package de.hidin.linktreebackendservice.image;

import java.io.IOException;

public class StorageException extends Exception {
    public StorageException(String s, IOException e) {
        super(s, e);
    }

    public StorageException(String s) {
        super(s);
    }
}
