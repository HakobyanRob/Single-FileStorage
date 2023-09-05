package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.result.ResultDTO;
import com.github.hakobyanrob.services.common.StoragePropertiesManager;
import com.github.hakobyanrob.services.storageDefinition.StorageDefinitionManager;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class ManipulationTest {
    protected static StoragePropertiesManager storagePropertiesManager;
    protected static StorageDefinitionManager singleFileStorageDefinitionManager;
    protected static StorageManipulationManager storageManipulationManager;

    protected void assertAdd(File testFile) {
        ResultDTO<File> addResult = storageManipulationManager.addFile(testFile);
        Assertions.assertTrue(addResult.isSuccessful());
        Assertions.assertNull(addResult.getErrorMessage());
        Assertions.assertEquals(testFile, addResult.getDto());
    }

    protected void assertFileExists(File testFile) throws IOException {
        ResultDTO<byte[]> getResult = storageManipulationManager.getFile(testFile.getName());
        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNotNull(getResult.getDto());
        Assertions.assertArrayEquals(Files.readAllBytes(testFile.toPath()), getResult.getDto());
    }

    protected void assertFileExistsWithDifferentContent(File testFile) throws IOException {
        ResultDTO<byte[]> getResult = storageManipulationManager.getFile(testFile.getName());
        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNotNull(getResult.getDto());
        Assertions.assertFalse(Arrays.equals(Files.readAllBytes(testFile.toPath()), getResult.getDto()));
    }

    protected void assertUpdate(File testFile) {
        ResultDTO<File> updateResult = storageManipulationManager.updateFile(testFile);
        Assertions.assertTrue(updateResult.isSuccessful());
        Assertions.assertNull(updateResult.getErrorMessage());
        Assertions.assertEquals(testFile, updateResult.getDto());
    }

    protected void assertDelete(File testFile) {
        Result deleteResult = storageManipulationManager.deleteFile(testFile.getName());
        Assertions.assertTrue(deleteResult.isSuccessful());
        Assertions.assertNull(deleteResult.getErrorMessage());
    }

    protected void assertFileNotExists(File testFile) {
        ResultDTO<byte[]> getResult = storageManipulationManager.getFile(testFile.getName());
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("File not found: " + testFile.getName(), getResult.getErrorMessage());
        Assertions.assertNull(getResult.getDto());
    }
}
