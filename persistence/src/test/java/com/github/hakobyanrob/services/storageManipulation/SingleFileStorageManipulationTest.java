package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.result.ManipulationManagerResult;
import com.github.hakobyanrob.services.common.StoragePropertiesManager;
import com.github.hakobyanrob.services.storageDefinition.SingleFileStorageDefinitionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;

public class SingleFileStorageManipulationTest extends ManipulationTest {

    private final static String resourceFilePath = "src/test/resources/storageManipulation/";
    private final static String updatedFilePath = "toUpdate/";

    @BeforeAll
    static void createStorage() {
        storagePropertiesManager = new StoragePropertiesManager(resourceFilePath + "testStorage.properties");
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager(resourceFilePath + storagePropertiesManager.getStoragePath());
        storageManipulationManager = new SingleFileStorageManipulationManager(singleFileStorageDefinitionManager);
        singleFileStorageDefinitionManager.createStorage();
    }

    private final String txtTestFile = "text.txt";
    private final String pngTestFile = "json.json";
    private final String jsonTestFile = "image.png";
    private final String audioTestFile = "audio.mp3";
    private final String xlsxTestFile = "excel.xlsx";
    private final String zipTestFile = "resources.zip";
    private final String charactersTestFile = "characters.txt";
    private final String withSpecialCharacterTestFile = "filewith\uF07Cinname.txt";
    private final String emptyFile = "";
    private final String spaceFile = "   ";
    private final String nonExistentFile = "notExistingFile";

    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
    void fullFlowPositiveTest(String fileName) throws IOException {
        String testFilePath = resourceFilePath + fileName;
        File testFile = new File(testFilePath);
        Assertions.assertTrue(testFile.exists(), "Test file does not exist: " + testFilePath);

        String updateTestFilePath = resourceFilePath + updatedFilePath + fileName;
        File updateTestFile = new File(updateTestFilePath);
        Assertions.assertTrue(updateTestFile.exists(), "Test file does not exist: " + updateTestFile);

        assertAdd(testFile);
        assertFileExists(testFile);

        assertUpdate(updateTestFile);
        assertFileExists(updateTestFile);
        assertFileExistsWithDifferentContent(testFile);

        assertDelete(updateTestFile);
        assertFileNotExists(updateTestFile);
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

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile, nonExistentFile})
    void updateEmptyFile(String filePath) {
        ManipulationManagerResult addResult = storageManipulationManager.updateFile(new File(filePath));
        Assertions.assertFalse(addResult.isSuccessful());
        Assertions.assertEquals("Invalid file provided", addResult.getError());
    }

    @Test
    void updateNullFile() {
        ManipulationManagerResult addResult = storageManipulationManager.updateFile(null);
        Assertions.assertFalse(addResult.isSuccessful());
        Assertions.assertEquals("Invalid file provided", addResult.getError());
    }

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile})
    void deleteEmptyFileName(String fileName) {
        ManipulationManagerResult getResult = storageManipulationManager.deleteFile(fileName);
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", getResult.getError());
        Assertions.assertNull(getResult.getFile());
    }

    @Test
    void deleteNullFileName() {
        ManipulationManagerResult getResult = storageManipulationManager.deleteFile(null);
        Assertions.assertFalse(getResult.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", getResult.getError());
        Assertions.assertNull(getResult.getFile());
    }

    @ParameterizedTest
    @ValueSource(strings = {nonExistentFile})
    void deleteNonExistentFileName(String fileName) {
        ManipulationManagerResult getResult = storageManipulationManager.deleteFile(fileName);
        Assertions.assertTrue(getResult.isSuccessful());
        Assertions.assertNull(getResult.getError());
        Assertions.assertNull(getResult.getFile());
    }

    @AfterAll
    static void deleteStorage() {
        Assertions.assertTrue(singleFileStorageDefinitionManager.deleteStorage().isSuccessful());
    }
}
