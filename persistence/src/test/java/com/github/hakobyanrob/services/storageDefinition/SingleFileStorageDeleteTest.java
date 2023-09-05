package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SingleFileStorageDeleteTest {
    private final StorageDefinitionManager singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager("testFilePath");

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
