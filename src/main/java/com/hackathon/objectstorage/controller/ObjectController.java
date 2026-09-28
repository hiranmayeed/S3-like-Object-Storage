package com.hackathon.objectstorage.controller;

import com.hackathon.objectstorage.storage.ReplicatedStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/objects")
public class ObjectController {

    private final ReplicatedStorageService storageService;

    public ObjectController() {
        this.storageService = new ReplicatedStorageService();
    }

    @PutMapping("/{objectId}")
    public ResponseEntity<String> uploadObject(
            @PathVariable String objectId,
            @RequestBody byte[] data) {

        try {
            storageService.saveObject(objectId, data);
            return ResponseEntity.ok("Object stored successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to store object");
        }
    }

    @GetMapping("/{objectId}")
    public ResponseEntity<byte[]> getObject(
            @PathVariable String objectId) {

        try {
            byte[] data = storageService.getObject(objectId);
            return ResponseEntity.ok(data);
        } catch (java.nio.file.NoSuchFileException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{objectId}")
    public ResponseEntity<String> deleteObject(
            @PathVariable String objectId) {

        try {
            storageService.deleteObject(objectId);
            return ResponseEntity.ok("Object deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to delete object");
        }
    }
}