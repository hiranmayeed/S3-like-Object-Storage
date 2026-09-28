package com.hackathon.objectstorage.storage;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChecksumUtilTest {

    @Test
    void shouldCalculateSha256Checksum() {

        byte[] data = "Hello S3".getBytes(StandardCharsets.UTF_8);

        String checksum = ChecksumUtil.sha256(data);

        assertEquals(
                "c9f7ed78c073c16bcb2f76fa4a5739cb6cf81677d32fdbeda1d69350d107b6f3",
                checksum
        );
    }
}