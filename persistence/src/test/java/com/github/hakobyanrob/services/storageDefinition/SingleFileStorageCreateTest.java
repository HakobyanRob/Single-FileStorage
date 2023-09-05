package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.ResultDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class SingleFileStorageCreateTest {

    private StorageDefinitionManager singleFileStorageDefinitionManager;
    private final String testFilePath = "testFilePath";

    @Test
    void testCreateSingleFileStorage_Success() {
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(testFilePath);
        ResultDTO<File> resultDTO = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(resultDTO.isSuccessful());
        Assertions.assertNull(resultDTO.getErrorMessage());
    }

    @Test
    void testCreateSingleFileStorage_FileAlreadyExists() throws IOException {
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(testFilePath);

        File file = new File(testFilePath);
        Assertions.assertTrue(file.createNewFile(), "Failed to create test file: " + testFilePath);

        ResultDTO<File> resultDTO = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(resultDTO.isSuccessful());
        Assertions.assertTrue(file.delete(), "Failed to delete test file: " + testFilePath);
    }

    @Test
    void testCreateSingleFileStorage_StorageFileAlreadyExists() {
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(testFilePath);

        singleFileStorageDefinitionManager.createStorage();

        ResultDTO<File> resultDTO = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(resultDTO.isSuccessful());
        Assertions.assertNull(resultDTO.getErrorMessage());
    }

    @Test
    void testCreateSingleFileStorage_InvalidPath() {
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager("");
        ResultDTO<File> storageCreationResult = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertFalse(storageCreationResult.isSuccessful());
        String expectedErrorMessage = "The system cannot find the path specified";
        Assertions.assertEquals(expectedErrorMessage, storageCreationResult.getErrorMessage());
    }

    @AfterEach
    void cleanUp() {
        singleFileStorageDefinitionManager.deleteStorage();
    }
}