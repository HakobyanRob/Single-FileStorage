package com.github.hakobyanrob.services.singlefilestorage;

import com.github.hakobyanrob.result.StorageManagerResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SingleFileStorageDeletionTest {
    private final StorageManager singleFileStorageManager = new SingleFileStorageManager();

    @Test
    void testDeleteSingleFileStorage_Success() {
        String filePath = "testFilePath";

        singleFileStorageManager.createStorage(filePath);

        StorageManagerResult storageCreationResult = singleFileStorageManager.deleteStorage();

        Assertions.assertTrue(storageCreationResult.isSuccessful());
        Assertions.assertNull(storageCreationResult.getError());
    }

    @Test
    void testDeleteSingleFileStorage_FileDoesNotExist() {
        StorageManagerResult storageCreationResult = singleFileStorageManager.deleteStorage();

        Assertions.assertFalse(storageCreationResult.isSuccessful());
        String expectedErrorMessage = "File Storage does not exist";
        Assertions.assertEquals(expectedErrorMessage, storageCreationResult.getError());
    }

    @AfterEach
    void cleanUp() {
        singleFileStorageManager.deleteStorage();
    }
}
