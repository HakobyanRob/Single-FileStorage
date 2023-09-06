package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.result.ResultDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;

public class SingleFileStorageManipulationTestNegative extends ManipulationTest {
    private final String emptyFile = "";
    private final String spaceFile = "   ";
    private final String nonExistentFile = "notExistingFile";

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile, nonExistentFile})
    void saveFileNegative(String filePath) {
        ResultDTO<File> resultDTO = storageManipulationManager.addFile(new File(filePath));
        Assertions.assertFalse(resultDTO.isSuccessful());
        Assertions.assertEquals("Invalid file provided", resultDTO.getErrorMessage());
        Assertions.assertNull(resultDTO.getDto());
    }

    @Test
    void saveNullFile() {
        ResultDTO<File> resultDto = storageManipulationManager.addFile(null);
        Assertions.assertFalse(resultDto.isSuccessful());
        Assertions.assertEquals("Invalid file provided", resultDto.getErrorMessage());
        Assertions.assertNull(resultDto.getDto());
    }

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile})
    void getEmptyFileName(String fileName) {
        ResultDTO<byte[]> resultDTO = storageManipulationManager.getFile(fileName);
        Assertions.assertFalse(resultDTO.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", resultDTO.getErrorMessage());
        Assertions.assertNull(resultDTO.getDto());
    }

    @Test
    void getNullFileName() {
        ResultDTO<byte[]> resultDTO = storageManipulationManager.getFile(null);
        Assertions.assertFalse(resultDTO.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", resultDTO.getErrorMessage());
        Assertions.assertNull(resultDTO.getDto());
    }

    @ParameterizedTest
    @ValueSource(strings = {nonExistentFile})
    void getNonExistentFileName(String fileName) {
        ResultDTO<byte[]> resultDTO = storageManipulationManager.getFile(fileName);
        Assertions.assertFalse(resultDTO.isSuccessful());
        Assertions.assertEquals("File not found: " + fileName, resultDTO.getErrorMessage());
        Assertions.assertNull(resultDTO.getDto());
    }

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile, nonExistentFile})
    void updateEmptyFile(String filePath) {
        ResultDTO<File> resultDTO = storageManipulationManager.updateFile(new File(filePath));
        Assertions.assertFalse(resultDTO.isSuccessful());
        Assertions.assertEquals("Invalid file provided", resultDTO.getErrorMessage());
        Assertions.assertNull(resultDTO.getDto());
    }

    @Test
    void updateNullFile() {
        ResultDTO<File> resultDTO = storageManipulationManager.updateFile(null);
        Assertions.assertFalse(resultDTO.isSuccessful());
        Assertions.assertEquals("Invalid file provided", resultDTO.getErrorMessage());
        Assertions.assertNull(resultDTO.getDto());
    }

    @ParameterizedTest
    @ValueSource(strings = {emptyFile, spaceFile})
    void deleteEmptyFileName(String fileName) {
        Result result = storageManipulationManager.deleteFile(fileName);
        Assertions.assertFalse(result.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", result.getErrorMessage());
    }

    @Test
    void deleteNullFileName() {
        Result result = storageManipulationManager.deleteFile(null);
        Assertions.assertFalse(result.isSuccessful());
        Assertions.assertEquals("Invalid file name provided", result.getErrorMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {nonExistentFile})
    void deleteNonExistentFileName(String fileName) {
        Result result = storageManipulationManager.deleteFile(fileName);
        Assertions.assertFalse(result.isSuccessful());
        Assertions.assertEquals("File not found: " + fileName, result.getErrorMessage());
    }

    @AfterAll
    static void deleteStorage() {
        Assertions.assertTrue(singleFileStorageDefinitionManager.deleteStorage().isSuccessful());
    }
}
