package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.ResultDTO;
import com.github.hakobyanrob.services.common.StoragePropertiesManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class SingleFileStorageCreateTest {

    private StoragePropertiesManager storagePropertiesManager;
    private StorageDefinitionManager singleFileStorageDefinitionManager;

    @Test
    void testCreateSingleFileStorage_Success() {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/storageDefinition/testStorage.properties");
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(storagePropertiesManager);
        ResultDTO<File> resultDTO = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(resultDTO.isSuccessful());
        Assertions.assertNull(resultDTO.getErrorMessage());
    }

    @Test
    void testCreateSingleFileStorageInFolder_Success() {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/storageDefinition/testStorageWithFolders.properties");
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(storagePropertiesManager);
        ResultDTO<File> resultDTO = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(resultDTO.isSuccessful());
        Assertions.assertNull(resultDTO.getErrorMessage());
    }

    @Test
    void testCreateSingleFileStorage_FileAlreadyExists() throws IOException {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/storageDefinition/testStorage.properties");
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(storagePropertiesManager);

        File file = new File(storagePropertiesManager.getStoragePath());
        Assertions.assertTrue(file.createNewFile(), "Failed to create test file: " + storagePropertiesManager.getStoragePath());

        ResultDTO<File> resultDTO = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(resultDTO.isSuccessful());
        Assertions.assertTrue(file.delete(), "Failed to delete test file: " + storagePropertiesManager.getStoragePath());
    }

    @Test
    void testCreateSingleFileStorage_StorageFileAlreadyExists() {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/storageDefinition/testStorage.properties");
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(storagePropertiesManager);

        singleFileStorageDefinitionManager.createStorage();

        ResultDTO<File> resultDTO = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(resultDTO.isSuccessful());
        Assertions.assertNull(resultDTO.getErrorMessage());
    }

    @Test
    void testCreateSingleFileStorage_InvalidPath() {
        storagePropertiesManager = new StoragePropertiesManager("src/test/resources/storageDefinition/emptyPathStorage.properties");
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(storagePropertiesManager);
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