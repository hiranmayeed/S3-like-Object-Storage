package com.example.demo.service;

import com.example.demo.entity.Bucket;
import com.example.demo.entity.StoredObject;
import com.example.demo.repository.BucketRepository;
import com.example.demo.repository.StoredObjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ObjectService {

    private final BucketRepository bucketRepository;
    private final StoredObjectRepository objectRepository;

    public ObjectService(
            BucketRepository bucketRepository,
            StoredObjectRepository objectRepository) {

        this.bucketRepository = bucketRepository;
        this.objectRepository = objectRepository;
    }

    public StoredObject createObject(
            String bucketName,
            String objectName,
            long size,
            String checksum,
            String storagePath) {

        Bucket bucket = bucketRepository.findByName(bucketName)
                .orElseThrow(() -> new RuntimeException("Bucket not found"));

        // Version numbers continue increasing even if an older version
        // has been soft-deleted.
        int nextVersion = objectRepository
                .findFirstByBucketAndObjectNameOrderByVersionDesc(
                        bucket, objectName)
                .map(object -> object.getVersion() + 1)
                .orElse(1);

        StoredObject object = new StoredObject();

        object.setBucket(bucket);
        object.setObjectName(objectName);
        object.setVersion(nextVersion);
        object.setSize(size);
        object.setChecksum(checksum);
        object.setStoragePath(storagePath);
        object.setCreatedAt(LocalDateTime.now());
        object.setDeleted(false);

        return objectRepository.save(object);
    }

    public List<StoredObject> getObjects(String bucketName) {

        Bucket bucket = bucketRepository.findByName(bucketName)
                .orElseThrow(() -> new RuntimeException("Bucket not found"));

        // Do not return soft-deleted objects to the frontend.
        return objectRepository.findByBucketAndDeletedFalse(bucket);
    }

    public StoredObject getLatestObject(
            String bucketName,
            String objectName) {

        Bucket bucket = bucketRepository.findByName(bucketName)
                .orElseThrow(() -> new RuntimeException("Bucket not found"));

        return objectRepository
                .findFirstByBucketAndObjectNameOrderByVersionDesc(
                        bucket, objectName)
                .filter(object -> !object.isDeleted())
                .orElseThrow(() -> new RuntimeException("Object not found"));
    }

    public List<StoredObject> getVersions(
            String bucketName,
            String objectName) {

        Bucket bucket = bucketRepository.findByName(bucketName)
                .orElseThrow(() -> new RuntimeException("Bucket not found"));

        // Version history is retained, including soft-deleted versions.
        return objectRepository.findByBucketAndObjectName(
                bucket, objectName);
    }

    public void deleteObject(String bucketName, String objectName) {

        Bucket bucket = bucketRepository.findByName(bucketName)
                .orElseThrow(() -> new RuntimeException("Bucket not found"));

        StoredObject object = objectRepository
                .findFirstByBucketAndObjectNameOrderByVersionDesc(
                        bucket, objectName)
                .filter(currentObject -> !currentObject.isDeleted())
                .orElseThrow(() -> new RuntimeException("Object not found"));

        // Soft delete: retain the record and version history,
        // but hide the object from the active object listing.
        object.setDeleted(true);
        objectRepository.save(object);
    }
}