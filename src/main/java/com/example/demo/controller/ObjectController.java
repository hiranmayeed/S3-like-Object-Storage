package com.example.demo.controller;

import com.example.demo.entity.StoredObject;
import com.example.demo.service.ObjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buckets/{bucketName}/objects")
public class ObjectController {

    private final ObjectService objectService;

    public ObjectController(ObjectService objectService) {
        this.objectService = objectService;
    }

    @PostMapping("/{objectName}")
    public StoredObject createObject(
            @PathVariable String bucketName,
            @PathVariable String objectName,
            @RequestParam(defaultValue = "0") long size,
            @RequestParam(defaultValue = "") String checksum) {

        // Person 2 will later provide the real storage path.
        String storagePath = "PENDING_STORAGE";

        return objectService.createObject(
                bucketName,
                objectName,
                size,
                checksum,
                storagePath
        );
    }

    @GetMapping
    public List<StoredObject> getObjects(
            @PathVariable String bucketName) {

        return objectService.getObjects(bucketName);
    }

    @GetMapping("/{objectName}")
    public StoredObject getObject(
            @PathVariable String bucketName,
            @PathVariable String objectName) {

        return objectService.getLatestObject(
                bucketName,
                objectName
        );
    }

    @GetMapping("/{objectName}/versions")
    public List<StoredObject> getVersions(
            @PathVariable String bucketName,
            @PathVariable String objectName) {

        return objectService.getVersions(
                bucketName,
                objectName
        );
    }
}