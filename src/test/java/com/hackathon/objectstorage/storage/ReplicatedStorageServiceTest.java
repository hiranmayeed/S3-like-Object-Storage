package com.hackathon.objectstorage.storage;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplicatedStorageServiceTest {

    @Test
    void shouldReplicateObjectToAllThreeNodes() throws Exception {
        ReplicatedStorageService storageService =
                new ReplicatedStorageService();

        byte[] data = "Hello Replication".getBytes(StandardCharsets.UTF_8);

        storageService.saveObject("replicated-test.txt", data);

        Path node1Path =
                Path.of("storage-data", "node1", "replicated-test.txt");

        Path node2Path =
                Path.of("storage-data", "node2", "replicated-test.txt");

        Path node3Path =
                Path.of("storage-data", "node3", "replicated-test.txt");

        assertTrue(Files.exists(node1Path));
        assertTrue(Files.exists(node2Path));
        assertTrue(Files.exists(node3Path));

        assertArrayEquals(data, Files.readAllBytes(node1Path));
        assertArrayEquals(data, Files.readAllBytes(node2Path));
        assertArrayEquals(data, Files.readAllBytes(node3Path));
    }

    @Test
    void shouldGetObjectFromNode1() throws Exception {
        ReplicatedStorageService storageService =
                new ReplicatedStorageService();

        byte[] data = "Hello S3".getBytes(StandardCharsets.UTF_8);

        storageService.saveObject("get-test.txt", data);

        byte[] retrieved =
                storageService.getObject("get-test.txt");

        assertArrayEquals(data, retrieved);
    }

    @Test
    void shouldDeleteObjectFromAllThreeNodes() throws Exception {
        ReplicatedStorageService storageService =
                new ReplicatedStorageService();

        byte[] data = "Delete me".getBytes(StandardCharsets.UTF_8);

        storageService.saveObject("delete-replicated.txt", data);

        storageService.deleteObject("delete-replicated.txt");

        Path node1Path =
                Path.of("storage-data", "node1", "delete-replicated.txt");

        Path node2Path =
                Path.of("storage-data", "node2", "delete-replicated.txt");

        Path node3Path =
                Path.of("storage-data", "node3", "delete-replicated.txt");

        assertTrue(Files.notExists(node1Path));
        assertTrue(Files.notExists(node2Path));
        assertTrue(Files.notExists(node3Path));
    }
    @Test
    void shouldFallbackToNode2WhenNode1DoesNotHaveObject() throws Exception {
        ReplicatedStorageService storageService =
                new ReplicatedStorageService();

        byte[] data = "Fallback test".getBytes(StandardCharsets.UTF_8);

        storageService.saveObject("fallback-test.txt", data);

        Path node1Path =
                Path.of("storage-data", "node1", "fallback-test.txt");

        Files.deleteIfExists(node1Path);


        byte[] retrieved =
                storageService.getObject("fallback-test.txt");

        assertArrayEquals(data, retrieved);
    }
}