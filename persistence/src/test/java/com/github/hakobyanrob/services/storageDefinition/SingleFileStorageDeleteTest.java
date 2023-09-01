package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.DefinitionManagerResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SingleFileStorageDeleteTest {
    private final StorageDefinitionManager singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager("testFilePath");

    @Test
    void testDeleteSingleFileStorage_Success() {
        singleFileStorageDefinitionManager.createStorage();

        DefinitionManagerResult storageCreationResult = singleFileStorageDefinitionManager.deleteStorage();

        Assertions.assertTrue(storageCreationResult.isSuccessful());
        Assertions.assertNull(storageCreationResult.getError());
    }

    @Test
    void testDeleteSingleFileStorage_FileDoesNotExist() {
        DefinitionManagerResult storageCreationResult = singleFileStorageDefinitionManager.deleteStorage();

        Assertions.assertFalse(storageCreationResult.isSuccessful());
        String expectedErrorMessage = "File Storage does not exist";
        Assertions.assertEquals(expectedErrorMessage, storageCreationResult.getError());
    }

    @AfterEach
    void cleanUp() {
        singleFileStorageDefinitionManager.deleteStorage();
    }
}
