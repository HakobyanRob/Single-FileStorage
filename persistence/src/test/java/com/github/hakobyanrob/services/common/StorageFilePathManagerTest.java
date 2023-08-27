package com.github.hakobyanrob.services.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StorageFilePathManagerTest {
    StorageFilePathManager storageFilePathManager;

    @Test
    public void loadStoragePath() {
        storageFilePathManager = new StorageFilePathManager("src/test/resources/common/testStorage.properties");
        storageFilePathManager.readStorageProperties();

        Assertions.assertEquals("teststorage.txt", storageFilePathManager.getStoragePath());
    }

    @Test
    public void loadStoragePath_EmptyFile() {
        storageFilePathManager = new StorageFilePathManager("src/test/resources/common/empty.properties");
        storageFilePathManager.readStorageProperties();

        Assertions.assertEquals("storage.txt", storageFilePathManager.getStoragePath());
    }

    @Test
    public void loadStoragePath_FakeFile() {
        storageFilePathManager = new StorageFilePathManager("src/test/resources/common/fake.dummy");
        storageFilePathManager.readStorageProperties();

        Assertions.assertEquals("storage.txt", storageFilePathManager.getStoragePath());
    }
}
