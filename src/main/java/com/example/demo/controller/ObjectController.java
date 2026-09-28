package com.example.demo.controller;

import com.example.demo.entity.StoredObject;
import com.example.demo.service.ObjectService;
import com.hackathon.objectstorage.storage.ChecksumUtil;
import com.hackathon.objectstorage.storage.ReplicatedStorageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("metadataObjectController")
@RequestMapping("/buckets/{bucketName}/objects")
public class ObjectController {

    private final ObjectService objectService;
    private final ReplicatedStorageService storageService;

    public ObjectController(
            ObjectService objectService,
            ReplicatedStorageService storageService) {

        this.objectService = objectService;
        this.storageService = storageService;
    }

    @PostMapping("/{objectName}")
    public ResponseEntity<?> createObject(
            @PathVariable String bucketName,
            @PathVariable String objectName,
            @RequestBody byte[] data) {

        try {
            String objectId = UUID.randomUUID().toString();

            String checksum = ChecksumUtil.sha256(data);

            storageService.saveObject(objectId, data);

            StoredObject object = objectService.createObject(
                    bucketName,
                    objectName,
                    data.length,
                    checksum,
                    objectId
            );

            return ResponseEntity.ok(object);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to store object: " + e.getMessage());
        }
    }

    @GetMapping
    public List<StoredObject> getObjects(
            @PathVariable String bucketName) {

        return objectService.getObjects(bucketName);
    }

    @GetMapping("/{objectName}")
    public ResponseEntity<?> getObject(
            @PathVariable String bucketName,
            @PathVariable String objectName) {

        try {
            StoredObject metadata =
                    objectService.getLatestObject(bucketName, objectName);

            byte[] data =
                    storageService.getObject(metadata.getStoragePath());

            return ResponseEntity.ok()
                    .header("Content-Type", "application/octet-stream")
                    .body(data);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{objectName}/versions")
    public List<StoredObject> getVersions(
            @PathVariable String bucketName,
            @PathVariable String objectName) {

        return objectService.getVersions(bucketName, objectName);
    }

    @DeleteMapping("/{objectName}")
    public ResponseEntity<?> deleteObject(
            @PathVariable String bucketName,
            @PathVariable String objectName) {
        try {
            objectService.deleteObject(bucketName, objectName);
            return ResponseEntity.ok("Object deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}