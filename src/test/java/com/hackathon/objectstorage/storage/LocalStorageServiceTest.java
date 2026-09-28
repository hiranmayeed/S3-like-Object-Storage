package com.hackathon.objectstorage.storage;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LocalStorageServiceTest {

    @Test
    void shouldSaveAndGetObject() throws Exception {
        LocalStorageService storageService = new LocalStorageService("node1");

        byte[] data = "Hello S3".getBytes(StandardCharsets.UTF_8);

        storageService.saveObject("test-object.txt", data);

        byte[] retrieved = storageService.getObject("test-object.txt");

        assertArrayEquals(data, retrieved);
    }
    @Test
    void shouldDeleteObject() throws Exception {
        LocalStorageService storageService = new LocalStorageService("node1");

        byte[] data = "Hello S3".getBytes(StandardCharsets.UTF_8);

        storageService.saveObject("delete-test.txt", data);
        storageService.deleteObject("delete-test.txt");

        java.nio.file.Path path =
                java.nio.file.Path.of("storage-data", "node1", "delete-test.txt");

        assertFalse(java.nio.file.Files.exists(path));
    }
}