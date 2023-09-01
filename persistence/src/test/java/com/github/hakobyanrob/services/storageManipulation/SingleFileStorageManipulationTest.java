package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.result.ManipulationManagerResult;
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

    @BeforeAll
    static void createStorage() {
        singleFileStorageDefinitionManager = new SingleFileStorageDefinitionManager("src/test/resources/storageManipulation/storage.txt");
        storageManipulationManager = new SingleFileStorageManipulationManager(singleFileStorageDefinitionManager);
        singleFileStorageDefinitionManager.createStorage();
    }

    private final String txtTestFile = "src/test/resources/storageManipulation/text.txt";
    private final String pngTestFile = "src/test/resources/storageManipulation/json.json";
    private final String jsonTestFile = "src/test/resources/storageManipulation/image.png";
    private final String audioTestFile = "src/test/resources/storageManipulation/audio.mp3";
    private final String xlsxTestFile = "src/test/resources/storageManipulation/excel.xlsx";
    private final String zipTestFile = "src/test/resources/storageManipulation/resources.zip";
    private final String charactersTestFile = "src/test/resources/storageManipulation/characters.txt";
    private final String withSpecialCharacterTestFile = "src/test/resources/storageManipulation/filewith\uF07Cinname.txt";
    private final String emptyFile = "";
    private final String spaceFile = "   ";
    private final String nonExistentFile = "notExistingFile";

    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
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
