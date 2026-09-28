package com.example.demo.service;

import com.example.demo.entity.Bucket;
import com.example.demo.repository.BucketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BucketService {

    private final BucketRepository bucketRepository;

    public BucketService(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    public Bucket createBucket(String name) {

        if (bucketRepository.existsByName(name)) {
            throw new RuntimeException("Bucket already exists");
        }

        Bucket bucket = new Bucket(name);

        return bucketRepository.save(bucket);
    }

    public List<Bucket> getAllBuckets() {
        return bucketRepository.findAll();
    }
}