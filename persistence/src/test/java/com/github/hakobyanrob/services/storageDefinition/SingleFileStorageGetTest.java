package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.ResultDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class SingleFileStorageGetTest {
    private final String testFilePath = "testFilePath";
    private StorageDefinitionManager singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(testFilePath);

    @Test
    void testCreateSingleFileStorage_Success() {
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(testFilePath);
        ResultDTO<File> storageCreationResult = singleFileStorageDefinitionManager.createStorage();

        Assertions.assertTrue(storageCreationResult.isSuccessful());
        Assertions.assertNull(storageCreationResult.getErrorMessage());

        ResultDTO<File> getResult = singleFileStorageDefinitionManager.getStorage();
        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNull(getResult.getErrorMessage());
        Assertions.assertEquals(testFilePath, getResult.getDto().getName());

        Assertions.assertTrue(singleFileStorageDefinitionManager.deleteStorage().isSuccessful());
    }

    @Test
    void testCreateSingleFileStorage_LoadStorage() throws IOException {
        File file = new File(testFilePath);
        Assertions.assertTrue(file.createNewFile());


        ResultDTO<File> getResult = singleFileStorageDefinitionManager.getStorage();

        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNull(getResult.getErrorMessage());
        Assertions.assertEquals(testFilePath, getResult.getDto().getName());

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
