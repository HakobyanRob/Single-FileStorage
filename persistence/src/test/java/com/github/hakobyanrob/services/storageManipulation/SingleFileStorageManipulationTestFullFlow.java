package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.services.common.StoragePropertiesManager;
import com.github.hakobyanrob.services.storageDefinition.SingleFileStorageDefinitionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingleFileStorageManipulationTestFullFlow extends ManipulationTest {

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

    @Order(1)
    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
    void addFileTest(String fileName) {
        String testFilePath = resourceFilePath + fileName;
        File testFile = new File(testFilePath);
        Assertions.assertTrue(testFile.exists(), "Test file does not exist: " + testFilePath);

        assertAdd(testFile);
    }

    @Order(2)
    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
    void getFileTest(String fileName) throws IOException {
        String testFilePath = resourceFilePath + fileName;
        File testFile = new File(testFilePath);
        Assertions.assertTrue(testFile.exists(), "Test file does not exist: " + testFilePath);

        assertFileExists(testFile);
    }

    @Order(3)
    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
    void updateFileTest(String fileName) {
        String updateTestFilePath = resourceFilePath + updatedFilePath + fileName;
        File updateTestFile = new File(updateTestFilePath);
        Assertions.assertTrue(updateTestFile.exists(), "Test file does not exist: " + updateTestFile);

        assertUpdate(updateTestFile);
    }

    @Order(4)
    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
    void getUpdatedFileTest(String fileName) throws IOException {
        String testFilePath = resourceFilePath + fileName;
        File testFile = new File(testFilePath);
        Assertions.assertTrue(testFile.exists(), "Test file does not exist: " + testFilePath);

        String updateTestFilePath = resourceFilePath + updatedFilePath + fileName;
        File updateTestFile = new File(updateTestFilePath);
        Assertions.assertTrue(updateTestFile.exists(), "Test file does not exist: " + updateTestFile);

        assertFileExists(updateTestFile);
        assertFileExistsWithDifferentContent(testFile);
    }

    @Order(5)
    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
    void deleteFileTest(String fileName) {
        String testFilePath = resourceFilePath + fileName;
        File testFile = new File(testFilePath);
        Assertions.assertTrue(testFile.exists(), "Test file does not exist: " + testFilePath);

        String updateTestFilePath = resourceFilePath + updatedFilePath + fileName;
        File updateTestFile = new File(updateTestFilePath);
        Assertions.assertTrue(updateTestFile.exists(), "Test file does not exist: " + updateTestFile);

        assertDelete(updateTestFile);
    }

    @Order(6)
    @ParameterizedTest
    @ValueSource(strings = {txtTestFile, pngTestFile, jsonTestFile,
            audioTestFile, xlsxTestFile, charactersTestFile, zipTestFile, withSpecialCharacterTestFile})
    void getDeletedFileTest(String fileName) {
        String testFilePath = resourceFilePath + fileName;
        File testFile = new File(testFilePath);
        Assertions.assertTrue(testFile.exists(), "Test file does not exist: " + testFilePath);

        String updateTestFilePath = resourceFilePath + updatedFilePath + fileName;
        File updateTestFile = new File(updateTestFilePath);
        Assertions.assertTrue(updateTestFile.exists(), "Test file does not exist: " + updateTestFile);

        assertFileNotExists(updateTestFile);
    }

    @AfterAll
    static void deleteStorage() {
        Assertions.assertTrue(singleFileStorageDefinitionManager.deleteStorage().isSuccessful());
    }
}
