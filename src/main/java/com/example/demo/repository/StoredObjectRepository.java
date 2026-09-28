package com.example.demo.repository;

import com.example.demo.entity.Bucket;
import com.example.demo.entity.StoredObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoredObjectRepository extends JpaRepository<StoredObject, Long> {

    List<StoredObject> findByBucket(Bucket bucket);

    List<StoredObject> findByBucketAndObjectName(
            Bucket bucket,
            String objectName
    );

    Optional<StoredObject> findFirstByBucketAndObjectNameOrderByVersionDesc(
            Bucket bucket,
            String objectName
    );
}
