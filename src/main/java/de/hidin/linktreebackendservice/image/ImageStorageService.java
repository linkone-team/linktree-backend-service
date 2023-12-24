package de.hidin.linktreebackendservice.image;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class ImageStorageService {
    private final Path rootLocation; // Define the directory where images will be stored

    @Autowired
    public ImageStorageService(@Value("${image.storage.location}") String storagePath) throws StorageException {
        this.rootLocation = Paths.get(storagePath);
        createDirectoriesIfNeeded();
    }

    private void createDirectoriesIfNeeded() throws StorageException {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    public String storeImage(MultipartFile file, String slug) throws StorageException {
        String filename = slug + ".png"; // Set the extension to .png
        Path destinationFile = rootLocation.resolve(Paths.get(filename))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside current directory.");
        }

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            // Convert image to PNG
            try (InputStream input = file.getInputStream()) {
                BufferedImage image = ImageIO.read(input);
                if (image == null) {
                    throw new StorageException("Invalid image file.");
                }
                ImageIO.write(image, "png", new File(destinationFile.toString()));
            }

            return destinationFile.toString();
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
