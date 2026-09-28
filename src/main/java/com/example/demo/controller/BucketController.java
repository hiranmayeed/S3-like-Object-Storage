package com.example.demo.controller;

import com.example.demo.entity.Bucket;
import com.example.demo.service.BucketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buckets")
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping
    public Bucket createBucket(@RequestParam String name) {
        return bucketService.createBucket(name);
    }

    @GetMapping
    public List<Bucket> getBuckets() {
        return bucketService.getAllBuckets();
    }
}
