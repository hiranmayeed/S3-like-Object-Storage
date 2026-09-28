package com.hackathon.objectstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.hackathon.objectstorage",
        "com.example.demo"
})
public class ObjectStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObjectStorageApplication.class, args);
    }
}