package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.ResultDTO;
import com.github.hakobyanrob.services.common.StoragePropertiesManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class SingleFileStorageGetTest {
    private static StorageDefinitionManager singleFileStorageDefinitionManager;
    private static StoragePropertiesManager propertiesManager;

    @BeforeAll
    static void createStorage() {
        String storagePropertiesPath = "src/test/resources/storageDefinition/testStorage.properties";
        propertiesManager = new StoragePropertiesManager(storagePropertiesPath);
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(propertiesManager);
    }

    @Test
    void testCreateSingleFileStorage_Success() {
        ResultDTO<File> storageCreationResult = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(storageCreationResult.isSuccessful());
        Assertions.assertNull(storageCreationResult.getErrorMessage());

        ResultDTO<File> getResult = singleFileStorageDefinitionManager.getStorage();
        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNull(getResult.getErrorMessage());
        Assertions.assertEquals(propertiesManager.getStoragePath(), getResult.getDto().getName());

        Assertions.assertTrue(singleFileStorageDefinitionManager.deleteStorage().isSuccessful());
    }

    @Test
    void testCreateSingleFileStorage_LoadStorage() throws IOException {
        File file = new File(propertiesManager.getStoragePath());
        Assertions.assertTrue(file.createNewFile());


        ResultDTO<File> getResult = singleFileStorageDefinitionManager.getStorage();

        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNull(getResult.getErrorMessage());
        Assertions.assertEquals(propertiesManager.getStoragePath(), getResult.getDto().getName());

        Assertions.assertTrue(singleFileStorageDefinitionManager.deleteStorage().isSuccessful());
    }

    @Test
    void testCreateSingleFileStorage_NonExistent() {
        ResultDTO<File> getResult = singleFileStorageDefinitionManager.getStorage();
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("Failed to load Storage", getResult.getErrorMessage());
        Assertions.assertNull(getResult.getDto());
    }
}
