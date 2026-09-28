package com.hackathon.objectstorage.storage;

public class ReplicatedStorageService implements StorageService {

    private final LocalStorageService node1;
    private final LocalStorageService node2;
    private final LocalStorageService node3;

    public ReplicatedStorageService() {
        this.node1 = new LocalStorageService("node1");
        this.node2 = new LocalStorageService("node2");
        this.node3 = new LocalStorageService("node3");
    }

    @Override
    public void saveObject(String objectId, byte[] data) throws java.io.IOException {
        node1.saveObject(objectId, data);
        node2.saveObject(objectId, data);
        node3.saveObject(objectId, data);
    }

    @Override
    public byte[] getObject(String objectId) throws java.io.IOException {
        return node1.getObject(objectId);
    }

    @Override
    public void deleteObject(String objectId) throws java.io.IOException {
        node1.deleteObject(objectId);
        node2.deleteObject(objectId);
        node3.deleteObject(objectId);
    }
}