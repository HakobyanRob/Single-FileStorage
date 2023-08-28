package com.github.hakobyanrob.services.file;

import com.github.hakobyanrob.result.ManipulationManagerResult;
import com.github.hakobyanrob.services.singlefilestorage.SingleFileStorageDefinitionManager;
import com.github.hakobyanrob.services.singlefilestorage.StorageDefinitionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SingleFileStorageManipulationTest {
    private static StorageDefinitionManager singleFileStorageDefinitionManager;
    private static StorageManipulationManager storageManipulationManager;

    private static final int BUFFER_SIZE = 4096;

    @BeforeAll
    static void createStorage() {
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager("src/test/resources/file/storage.txt");
        storageManipulationManager = new SingleFileStorageManipulationManager(singleFileStorageDefinitionManager);
        singleFileStorageDefinitionManager.createStorage();
    }

    private final String txtTestFile = "src/test/resources/file/text.txt";
    private final String pngTestFile = "src/test/resources/file/json.json";
    private final String jsonTestFile = "src/test/resources/file/image.png";
    private final String audioTestFile = "src/test/resources/file/audio.mp3";
    private final String xlsxTestFile = "src/test/resources/file/excel.xlsx";
    private final String zipTestFile = "src/test/resources/file/resources.zip";
    private final String charactersTestFile = "src/test/resources/file/characters.txt";
    private final String emptyFile = "";
    private final String spaceFile = "   ";
    private final String nonExistentFile = "notExistingFile";

    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile})
    void addGetDeleteFilePositive(String filePath) throws IOException {
        File testFile = new File(filePath);
        Assertions.assertTrue(testFile.exists(), "Test file does not exist: " + filePath);

        assertCreation(testFile);

        assertFileExists(testFile);

        assertDeletion(testFile);

        assertFileNotExists(testFile);
    }

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile, nonExistentFile})
    void saveFileNegative(String filePath) {
        ManipulationManagerResult addResult = storageManipulationManager.addFile(new File(filePath));
        Assertions.assertFalse(addResult.isSuccessful());
        Assertions.assertEquals("Invalid file provided", addResult.getError());
    }

    @Test
    void saveNullFile() {
        ManipulationManagerResult addResult = storageManipulationManager.addFile(null);
        Assertions.assertFalse(addResult.isSuccessful());
        Assertions.assertEquals("Invalid file provided", addResult.getError());
    }

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile})
    void getEmptyFileName(String fileName) {
        ManipulationManagerResult getResult = storageManipulationManager.getFile(fileName);
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", getResult.getError());
        Assertions.assertNull(getResult.getFile());
    }

    @Test
    void getNullFileName() {
        ManipulationManagerResult getResult = storageManipulationManager.getFile(null);
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", getResult.getError());
        Assertions.assertNull(getResult.getFile());
    }

    @ParameterizedTest
    @ValueSource(strings = {nonExistentFile})
    void getNonExistentFileName(String fileName) {
        ManipulationManagerResult getResult = storageManipulationManager.getFile(fileName);
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("File not found: " + fileName, getResult.getError());
        Assertions.assertNull(getResult.getFile());
    }

    //update and get test
    //delete

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

    private void assertCreation(File testFile) {
        ManipulationManagerResult manipulationManagerResult = storageManipulationManager.addFile(testFile);
        Assertions.assertTrue(manipulationManagerResult.isSuccessful());
        Assertions.assertNull(manipulationManagerResult.getError());
        Assertions.assertEquals(testFile, manipulationManagerResult.getFile());
    }

    private void assertFileExists(File testFile) throws IOException {
        ManipulationManagerResult getResult = storageManipulationManager.getFile(testFile.getName());
        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNotNull(getResult.file());
        Assertions.assertTrue(compareFiles(testFile, getResult.file()));
    }

    private void assertDeletion(File testFile) {
        ManipulationManagerResult manipulationManagerResult = storageManipulationManager.deleteFile(testFile.getName());
        Assertions.assertTrue(manipulationManagerResult.isSuccessful());
        Assertions.assertNull(manipulationManagerResult.getError());
        Assertions.assertNull(manipulationManagerResult.getFile());
    }

    private void assertFileNotExists(File testFile) {
        ManipulationManagerResult getResult = storageManipulationManager.getFile(testFile.getName());
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("File not found: " + testFile.getName(), getResult.getError());
        Assertions.assertNull(getResult.file());
    }

    @AfterAll
    static void deleteStorage() {
        Assertions.assertTrue(singleFileStorageDefinitionManager.deleteStorage().isSuccessful());
    }
}
