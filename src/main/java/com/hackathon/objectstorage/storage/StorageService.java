package com.hackathon.objectstorage.storage;

import java.io.IOException;

public interface StorageService {

    void saveObject(String objectId, byte[] data) throws IOException;

    byte[] getObject(String objectId) throws IOException;

    void deleteObject(String objectId) throws IOException;
}