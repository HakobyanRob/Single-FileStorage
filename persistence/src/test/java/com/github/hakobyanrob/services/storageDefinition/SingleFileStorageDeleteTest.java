package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.services.common.StoragePropertiesManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SingleFileStorageDeleteTest {

    private static StorageDefinitionManager singleFileStorageDefinitionManager;

    @BeforeAll
    static void createStorage() {
        String storagePropertiesPath = "src/test/resources/storageDefinition/testStorage.properties";
        StoragePropertiesManager propertiesManager = new StoragePropertiesManager(storagePropertiesPath);
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(propertiesManager);
    }

    @Test
    void testDeleteSingleFileStorage_Success() {
        singleFileStorageDefinitionManager.createStorage();

        Result result = singleFileStorageDefinitionManager.deleteStorage();

        Assertions.assertTrue(result.isSuccessful());
        Assertions.assertNull(result.getErrorMessage());
    }

    @Test
    void testDeleteSingleFileStorage_FileDoesNotExist() {
        Result result = singleFileStorageDefinitionManager.deleteStorage();

        Assertions.assertFalse(result.isSuccessful());
        String expectedErrorMessage = "File Storage does not exist";
        Assertions.assertEquals(expectedErrorMessage, result.getErrorMessage());
    }

    @AfterEach
    void cleanUp() {
        singleFileStorageDefinitionManager.deleteStorage();
    }
}
