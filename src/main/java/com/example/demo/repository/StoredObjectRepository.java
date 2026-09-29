package com.example.demo.repository;

import com.example.demo.entity.Bucket;
import com.example.demo.entity.StoredObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoredObjectRepository extends JpaRepository<StoredObject, Long> {

    // Return only objects that have not been soft-deleted
    List<StoredObject> findByBucketAndDeletedFalse(Bucket bucket);

    // Return all versions of an object, including deleted versions
    List<StoredObject> findByBucketAndObjectName(
            Bucket bucket,
            String objectName
    );

    // Find the latest version of an object
    Optional<StoredObject> findFirstByBucketAndObjectNameOrderByVersionDesc(
            Bucket bucket,
            String objectName
    );
}