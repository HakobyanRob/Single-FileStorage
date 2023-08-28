package com.github.hakobyanrob.services.file;

import com.github.hakobyanrob.result.ManipulationManagerResult;
import com.github.hakobyanrob.result.StorageManagerResult;
import com.github.hakobyanrob.services.singlefilestorage.StorageDefinitionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleFileStorageManipulationManager implements StorageManipulationManager {
    private static final Logger logger = Logger.getLogger(SingleFileStorageManipulationManager.class.getName());

    private final StorageDefinitionManager storageDefinitionManager;
    private static final String FILE_NAME_PREFIX = "fileName#";
    private static final String DELIMITER = "|";

    public SingleFileStorageManipulationManager(StorageDefinitionManager storageDefinitionManager) {
        this.storageDefinitionManager = storageDefinitionManager;
    }

    @Override
    public ManipulationManagerResult addFile(File file) {
        if (isInvalidFile(file)) {
            return new ManipulationManagerResult(false, "Invalid file provided", null);
        }

        StorageManagerResult managerResult = storageDefinitionManager.getStorage();
        if (!managerResult.isSuccessful() && !createStorage()) {
            return new ManipulationManagerResult(false, "Failed to create storage", null);
        }

        if (getFile(file.getName()).isSuccessful()) {
            return new ManipulationManagerResult(false, "File already exists with the same name", null);
        }

        try (FileWriter writer = new FileWriter(managerResult.getStorage(), true)) {
            String metadata = createMetadata(file);
            writer.write(metadata);

            logger.log(Level.INFO, "File added: " + file.getName());
            return new ManipulationManagerResult(true, null, file);
        } catch (IOException e) {
            return handleIOException("Failed to add the file: " + file.getName(), e);
        }
    }

    @Override
    public ManipulationManagerResult getFile(String fileName) {
        if (isInvalidFileName(fileName)) {
            return new ManipulationManagerResult(false, "Invalid file name provided", null);
        }

        StorageManagerResult managerResult = storageDefinitionManager.getStorage();
        if (!managerResult.isSuccessful()) {
            return new ManipulationManagerResult(false, "Failed to get storage", null);
        }

        try {
            File file = findFile(managerResult.getStorage(), fileName);
            if (file != null) {
                logger.log(Level.INFO, "Successfully found the file: " + fileName);
                return new ManipulationManagerResult(true, null, file);
            } else {
                return new ManipulationManagerResult(false, "File not found: " + fileName, null);
            }
        } catch (IOException e) {
            return handleIOException("Failed to retrieve the file: " + e, e);
        }
    }

    @Override
    public ManipulationManagerResult updateFile(File file) {
        if (isInvalidFile(file)) {
            return new ManipulationManagerResult(false, "Invalid file provided", null);
        }

        ManipulationManagerResult deleteResult = deleteFile(file.getName());
        if (!deleteResult.isSuccessful()) {
            return deleteResult;
        }

        return addFile(file);
    }

    @Override
    public ManipulationManagerResult deleteFile(String fileName) {
        if (isInvalidFileName(fileName)) {
            return new ManipulationManagerResult(false, "Invalid file name provided", null);
        }

        StorageManagerResult managerResult = storageDefinitionManager.getStorage();
        if (!managerResult.isSuccessful()) {
            return new ManipulationManagerResult(false, "Failed to get storage", null);
        }

        try {
            deleteFile(managerResult.getStorage(), fileName);
            return new ManipulationManagerResult(true, null, null);
        } catch (IOException e) {
            return handleIOException("Failed to delete the file: " + e, e);
        }
    }

    private boolean isInvalidFile(File file) {
        return file == null || !file.exists();
    }

    private boolean isInvalidFileName(String fileName) {
        return fileName == null || fileName.isBlank();
    }

    private boolean createStorage() {
        StorageManagerResult creationResult = storageDefinitionManager.createStorage();
        if (creationResult.isSuccessful()) {
            logger.log(Level.INFO, "Successfully created new storage");
            return true;
        } else {
            logger.log(Level.SEVERE, "Failed to create new storage");
            return false;
        }
    }

    private ManipulationManagerResult handleIOException(String message, IOException e) {
        logger.log(Level.SEVERE, message, e);
        return new ManipulationManagerResult(false, message, null);
    }

    private String createMetadata(File file) throws IOException {
        Path filePath = file.toPath();

        String encodedFile = encodeFile(filePath);

        // We can easily add file creation time and other attributes to the saved data using the following method:
        // BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
        String metadata = String.format(FILE_NAME_PREFIX + "%s" + DELIMITER + "%s",
                file.getName(), encodedFile.replace("\"", "\\\""));
        long size = metadata.length();

        return size + DELIMITER + metadata;
    }

    private String encodeFile(Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private File decodeFile(String fileName, String encodedFile) throws IOException {
        byte[] decode = Base64.getDecoder().decode(encodedFile);
        return convertByteArrayToFile(fileName, decode);
    }

    private File convertByteArrayToFile(String filePath, byte[] byteArray) throws IOException {
        File file = new File(filePath);
        file.deleteOnExit();
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(byteArray);
        outputStream.close();
        return file;
    }

    private File findFile(File storage, String targetFileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(storage))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int length = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                line = line.substring(line.indexOf(DELIMITER) + 1); // Skip the length and move to the next section

                String currentFileName = line.substring(line.indexOf(FILE_NAME_PREFIX) + 9, line.indexOf(DELIMITER, line.indexOf(FILE_NAME_PREFIX)));
                while (!currentFileName.equals(targetFileName) && length < line.length()) {
                    line = line.substring(length);
                    length = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                    line = line.substring(line.indexOf(DELIMITER) + 1);
                    currentFileName = line.substring(line.indexOf(FILE_NAME_PREFIX) + 9, line.indexOf(DELIMITER, line.indexOf(FILE_NAME_PREFIX)));
                }
                if (currentFileName.equals(targetFileName)) {
                    String encodedContents = line.substring(10 + targetFileName.length(), length);
                    return decodeFile(targetFileName, encodedContents);
                }
            }
        }
        return null;
    }

    private void deleteFile(File storage, String targetFileName) throws IOException {
        File tempFile = new File("temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(storage));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int length = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                line = line.substring(line.indexOf(DELIMITER) + 1);

                String currentLine = length + DELIMITER + line.substring(0, length);

                String content = line.substring(line.indexOf(FILE_NAME_PREFIX) + 9, line.indexOf(DELIMITER, line.indexOf(FILE_NAME_PREFIX)));
                if (!content.equals(targetFileName)) {
                    writer.write(currentLine);
                }
            }
        }

        File backup = new File("backup.txt");
        backup.deleteOnExit();
        Files.copy(storage.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);

        if (storage.delete() && tempFile.renameTo(storage)) {
            logger.log(Level.INFO, "Content removed successfully.");
        } else {
            logger.log(Level.WARNING, "Failed to remove content.");
            logger.log(Level.WARNING, "Reverting to back-up.");
            Files.copy(backup.toPath(), storage.toPath());
        }
    }
}
