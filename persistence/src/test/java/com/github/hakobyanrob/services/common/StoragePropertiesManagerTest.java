package com.github.hakobyanrob.services.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StoragePropertiesManagerTest {
    StoragePropertiesManager storagePropertiesManager;

    @Test
    public void loadStoragePath() {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/common/testStorage.properties");

        Assertions.assertEquals("teststorage.txt", storagePropertiesManager.getStoragePath());
        Assertions.assertEquals("testTemp.txt", storagePropertiesManager.getTempPath());
        Assertions.assertEquals("testBackup.txt", storagePropertiesManager.getBackupPath());
    }

    @Test
    public void loadStoragePath_EmptyFile() {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/common/empty.properties");

        Assertions.assertEquals("storage.txt", storagePropertiesManager.getStoragePath());
        Assertions.assertEquals("temp/temp.txt", storagePropertiesManager.getTempPath());
        Assertions.assertEquals("temp/backup.txt", storagePropertiesManager.getBackupPath());
    }

    @Test
    public void loadStoragePath_FakeFile() {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/common/fake.dummy");

        Assertions.assertEquals("storage.txt", storagePropertiesManager.getStoragePath());
        Assertions.assertEquals("temp/temp.txt", storagePropertiesManager.getTempPath());
        Assertions.assertEquals("temp/backup.txt", storagePropertiesManager.getBackupPath());
    }
}
