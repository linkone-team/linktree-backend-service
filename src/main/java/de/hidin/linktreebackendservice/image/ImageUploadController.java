package de.hidin.linktreebackendservice.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {

    private final ImageStorageService imageStorageService;
    private final Path rootLocation; // Directory where images are stored
    private final Path defaultLocation; // Directory where images are stored

    @Autowired
    public ImageUploadController(ImageStorageService imageStorageService,
                                 @Value("${image.storage.location}") String storagePath) {
        this.imageStorageService = imageStorageService;
        this.rootLocation = Paths.get(storagePath);
        this.defaultLocation = Paths.get("default/");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file,
                                         @RequestParam("slug") String slug) {
        try {
            String imagePath = imageStorageService.storeImage(file, slug);
            if (imagePath != null) {
                return ResponseEntity.ok("Image uploaded successfully!");
            }
            return ResponseEntity.ok("Image uploaded successfully!");
        } catch (StorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not upload the image: " + e.getMessage());
        }
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Resource> getImage(@PathVariable String slug) {
        try {
            Path file = rootLocation.resolve(slug + ".png");
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                Path file1 = defaultLocation.resolve("default.png");
                Resource resource1 = new UrlResource(file1.toUri());
                if (resource1.exists() || resource1.isReadable()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_PNG)
                            .body(resource1);
                } else {
                    throw new RuntimeException("Could not read the file!");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
