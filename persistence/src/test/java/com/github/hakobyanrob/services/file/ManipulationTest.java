package com.github.hakobyanrob.services.file;

import com.github.hakobyanrob.result.ManipulationManagerResult;
import com.github.hakobyanrob.services.singlefilestorage.StorageDefinitionManager;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ManipulationTest {
    protected static StorageDefinitionManager singleFileStorageDefinitionManager;
    protected static StorageManipulationManager storageManipulationManager;

    private static final int BUFFER_SIZE = 4096;

    public static boolean compareFiles(File filePath1, File filePath2) throws IOException {
        try (InputStream inputStream1 = new FileInputStream(filePath1);
             InputStream inputStream2 = new FileInputStream(filePath2)
        ) {
            byte[] buffer1 = new byte[BUFFER_SIZE];
            byte[] buffer2 = new byte[BUFFER_SIZE];
            int bytesRead1 = inputStream1.read(buffer1);
            int bytesRead2 = inputStream2.read(buffer2);

            if (bytesRead1 != bytesRead2 || notEqualArrays(buffer1, buffer2, bytesRead1)) {
                return false;
            }
            while (bytesRead1 != -1) {
                bytesRead1 = inputStream1.read(buffer1);
                bytesRead2 = inputStream2.read(buffer2);

                if (bytesRead1 != bytesRead2 || notEqualArrays(buffer1, buffer2, bytesRead1)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean notEqualArrays(byte[] arr1, byte[] arr2, int length) {
        for (int i = 0; i < length; i++) {
            if (arr1[i] != arr2[i]) {
                return true;
            }
        }
        return false;
    }

    protected void assertCreation(File testFile) {
        ManipulationManagerResult manipulationManagerResult = storageManipulationManager.addFile(testFile);
        Assertions.assertTrue(manipulationManagerResult.isSuccessful());
        Assertions.assertNull(manipulationManagerResult.getError());
        Assertions.assertEquals(testFile, manipulationManagerResult.getFile());
    }

    protected void assertFileExists(File testFile) throws IOException {
        ManipulationManagerResult getResult = storageManipulationManager.getFile(testFile.getName());
        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNotNull(getResult.file());
        Assertions.assertTrue(compareFiles(testFile, getResult.file()));
    }

    protected void assertDeletion(File testFile) {
        ManipulationManagerResult manipulationManagerResult = storageManipulationManager.deleteFile(testFile.getName());
        Assertions.assertTrue(manipulationManagerResult.isSuccessful());
        Assertions.assertNull(manipulationManagerResult.getError());
        Assertions.assertNull(manipulationManagerResult.getFile());
    }

    protected void assertFileNotExists(File testFile) {
        ManipulationManagerResult getResult = storageManipulationManager.getFile(testFile.getName());
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("File not found: " + testFile.getName(), getResult.getError());
        Assertions.assertNull(getResult.file());
    }
}
