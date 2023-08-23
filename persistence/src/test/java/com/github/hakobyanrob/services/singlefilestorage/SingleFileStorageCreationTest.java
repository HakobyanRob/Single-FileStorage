package com.github.hakobyanrob.services.singlefilestorage;

import com.github.hakobyanrob.result.StorageManagerResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class SingleFileStorageCreationTest {

    private final StorageManager singleFileStorageManager = new SingleFileStorageManager();

    @Test
    void testCreateSingleFileStorage_Success() {
        String filePath = "testFilePath";

        StorageManagerResult storageCreationResult = singleFileStorageManager.createStorage(filePath);

        Assertions.assertTrue(storageCreationResult.isSuccessful());
        Assertions.assertNull(storageCreationResult.getError());
    }

    @Test
    void testCreateSingleFileStorage_FileAlreadyExists() throws IOException {
        String filePath = "testFilePath";

        File file = new File(filePath);
        Assertions.assertTrue(file.createNewFile(), "Failed to create test file: " + filePath);

        StorageManagerResult storageCreationResult = singleFileStorageManager.createStorage(filePath);

        Assertions.assertFalse(storageCreationResult.isSuccessful());
        String expectedErrorMessage = "File with same name already exists at path: " + filePath;
        Assertions.assertEquals(expectedErrorMessage, storageCreationResult.getError());

        Assertions.assertTrue(file.delete(), "Failed to delete test file: " + filePath);
    }

    @Test
    void testCreateSingleFileStorage_StorageFileAlreadyExists() {
        String filePath = "testFilePath";

        singleFileStorageManager.createStorage(filePath);

        StorageManagerResult storageCreationResult = singleFileStorageManager.createStorage(filePath);

        Assertions.assertFalse(storageCreationResult.isSuccessful());
        String expectedErrorMessage = "File Storage already exists located at: " + filePath;
        Assertions.assertEquals(expectedErrorMessage, storageCreationResult.getError());
    }

    @Test
    void testCreateSingleFileStorage_InvalidPath() {
        String filePath = "";

        StorageManagerResult storageCreationResult = singleFileStorageManager.createStorage(filePath);

        Assertions.assertFalse(storageCreationResult.isSuccessful());
        String expectedErrorMessage = "The system cannot find the path specified";
        Assertions.assertEquals(expectedErrorMessage, storageCreationResult.getError());
    }

    @AfterEach
    void cleanUp() {
        singleFileStorageManager.deleteStorage();
    }
}