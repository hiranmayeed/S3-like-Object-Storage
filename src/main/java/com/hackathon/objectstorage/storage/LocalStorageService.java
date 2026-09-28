package com.hackathon.objectstorage.storage;

import java.io.IOException;

public class LocalStorageService implements StorageService {
    private final String nodeName;
    public LocalStorageService(String nodeName) {
        this.nodeName = nodeName;
    }
    @Override
    public void saveObject(String objectId, byte[] data) throws IOException {
        java.nio.file.Path path = java.nio.file.Path.of("storage-data", nodeName, objectId);
        java.nio.file.Files.createDirectories(path.getParent());
        java.nio.file.Files.write(path, data);
    }

    @Override
    public byte[] getObject(String objectId) throws IOException {
        java.nio.file.Path path =
                java.nio.file.Path.of("storage-data", nodeName, objectId);

        return java.nio.file.Files.readAllBytes(path);
    }

    @Override
    public void deleteObject(String objectId) throws IOException {
        java.nio.file.Path path = java.nio.file.Path.of("storage-data", nodeName, objectId);
        java.nio.file.Files.deleteIfExists(path);
    }
}