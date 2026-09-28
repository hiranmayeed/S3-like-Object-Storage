package com.hackathon.objectstorage.storage;

import java.io.IOException;

public class LocalStorageService implements StorageService {
    @Override
    public void saveObject(String objectId, byte[] data) throws IOException {
        java.nio.file.Path path = java.nio.file.Path.of("storage-data", "node1", objectId);
        java.nio.file.Files.createDirectories(path.getParent());
        java.nio.file.Files.write(path, data);
    }

    @Override
    public byte[] getObject(String objectId) throws IOException {
        java.nio.file.Path path = java.nio.file.Path.of("storage-data", "node1", objectId);
        return java.nio.file.Files.readAllBytes(path);
    }

    @Override
    public void deleteObject(String objectId) throws IOException {
        java.nio.file.Path path = java.nio.file.Path.of("storage-data", "node1", objectId);
        java.nio.file.Files.deleteIfExists(path);
    }
}