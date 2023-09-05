package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.result.ResultDTO;
import com.github.hakobyanrob.services.storageDefinition.StorageDefinitionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleFileStorageManipulationManager implements StorageManipulationManager {
    private static final Logger logger = Logger.getLogger(SingleFileStorageManipulationManager.class.getName());

    private final StorageDefinitionManager storageDefinitionManager;
    private static final String DELIMITER = "|";

    private final Lock readLock;
    private final Lock writeLock;

    public SingleFileStorageManipulationManager(StorageDefinitionManager storageDefinitionManager) {
        this.storageDefinitionManager = storageDefinitionManager;
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    @Override
    public ResultDTO<File> addFile(File file) {
        if (isInvalidFile(file)) {
            return new ResultDTO<>("Invalid file provided");
        }

        writeLock.lock();
        try {
            ResultDTO<File> managerResult = storageDefinitionManager.getStorage();
            if (!managerResult.isSuccessful() && !createStorage()) {
                return new ResultDTO<>("Failed to create storage");
            }

            if (getFile(file.getName()).isSuccessful()) {
                return new ResultDTO<>("File already exists with the same name");
            }

            try (FileWriter writer = new FileWriter(managerResult.getDto(), true)) {
                String metadata = createMetadata(file);
                writer.write(metadata);

                logger.log(Level.INFO, "File added: " + file.getName());
                return new ResultDTO<>(file);
            } catch (IOException e) {
                return new ResultDTO<>(handleIOException("An error occurred while doing something", e).getErrorMessage());
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public ResultDTO<byte[]> getFile(String fileName) {
        if (isInvalidFileName(fileName)) {
            return new ResultDTO<>("Invalid file name provided");
        }

        readLock.lock();
        try {
            ResultDTO<File> managerResult = storageDefinitionManager.getStorage();
            if (!managerResult.isSuccessful()) {
                return new ResultDTO<>("Failed to get storage");
            }

            try {
                byte[] contents = findFile(managerResult.getDto(), fileName);
                if (contents != null) {
                    logger.log(Level.INFO, "Successfully found the file: " + fileName);
                    return new ResultDTO<>(contents);
                } else {
                    return new ResultDTO<>("File not found: " + fileName);
                }
            } catch (IOException e) {
                return new ResultDTO<>(handleIOException("An error occurred while doing something", e).getErrorMessage());
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public ResultDTO<File> updateFile(File file) {
        if (isInvalidFile(file)) {
            return new ResultDTO<>("Invalid file provided");
        }

        Result deleteResult = deleteFile(file.getName());
        if (!deleteResult.isSuccessful()) {
            return new ResultDTO<>(deleteResult.errorMessage());
        }

        return addFile(file);
    }

    @Override
    public Result deleteFile(String fileName) {
        if (isInvalidFileName(fileName)) {
            return new Result("Invalid file name provided");
        }

        writeLock.lock();
        try {
            ResultDTO<File> managerResult = storageDefinitionManager.getStorage();
            if (!managerResult.isSuccessful()) {
                return new Result("Failed to get storage");
            }

            try {
                return deleteFile(managerResult.getDto(), fileName);
            } catch (IOException e) {
                return handleIOException("Failed to delete the file: " + e, e);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private boolean isInvalidFile(File file) {
        return file == null || !file.exists();
    }

    private boolean isInvalidFileName(String fileName) {
        return fileName == null || fileName.isBlank();
    }

    private boolean createStorage() {
        ResultDTO<File> creationResult = storageDefinitionManager.createStorage();
        if (creationResult.isSuccessful()) {
            logger.log(Level.INFO, "Successfully created new storage");
            return true;
        } else {
            logger.log(Level.SEVERE, "Failed to create new storage");
            return false;
        }
    }

    private Result handleIOException(String message, IOException e) {
        logger.log(Level.SEVERE, message, e);
        return new ResultDTO<>(message);
    }

    private String createMetadata(File file) throws IOException {
        Path filePath = file.toPath();

        String encodedFile = encodeFile(filePath);

        // We can easily add file creation time and other attributes to the saved data using the following method:
        // BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
        String metadata = String.format("%s" + DELIMITER + "%s",
                file.getName(), encodedFile.replace("\"", "\\\""));
        long size = metadata.length();

        return size + DELIMITER + metadata;
    }

    private String encodeFile(Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private byte[] decodeFile(String encodedFile) {
        return Base64.getDecoder().decode(encodedFile);
    }

    private byte[] findFile(File storage, String targetFileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(storage))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int length = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                line = line.substring(line.indexOf(DELIMITER) + 1); // Skip the length and move to the next section

                String currentFileName = line.substring(0, line.indexOf(DELIMITER));
                while (!currentFileName.equals(targetFileName) && length < line.length()) {
                    line = line.substring(length);
                    length = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                    line = line.substring(line.indexOf(DELIMITER) + 1);
                    currentFileName = line.substring(0, line.indexOf(DELIMITER));
                }
                if (currentFileName.equals(targetFileName)) {
                    String encodedContents = line.substring(targetFileName.length() + DELIMITER.length(), length);
                    return decodeFile(encodedContents);
                }
            }
        }
        return null;
    }


    private Result deleteFile(File storage, String targetFileName) throws IOException {
        Files.createDirectories(Paths.get("temp"));
        File tempFile = new File("temp/temp.txt");
        tempFile.deleteOnExit();
        boolean deletedFile = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(storage));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int length = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                while (length <= line.length()) {
                    line = line.substring(line.indexOf(DELIMITER) + 1);
                    String currentFileName = line.substring(0, line.indexOf(DELIMITER));

                    if (!currentFileName.equals(targetFileName)) {
                        StringBuilder currentFile = new StringBuilder();
                        String encodedContents = line.substring(currentFileName.length() + DELIMITER.length(), length);
                        currentFile.append(length).append(DELIMITER).append(currentFileName).append(DELIMITER).append(encodedContents);
                        writer.write(currentFile.toString());
                    } else {
                        deletedFile = true;
                    }
                    line = line.substring(length);
                    if (!line.isBlank()) {
                        length = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                    }
                }
            }
        }

        Result result;
        if (deletedFile) {
            File backup = new File("temp/backup.txt");
            backup.deleteOnExit();
            Files.copy(storage.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (storage.delete() && tempFile.renameTo(storage)) {
                logger.log(Level.INFO, "Content removed successfully.");
                result = new Result();
            } else {
                logger.log(Level.WARNING, "Failed to remove content.");
                logger.log(Level.WARNING, "Reverting to back-up.");
                Files.copy(backup.toPath(), storage.toPath());
                result = new Result("Failed to remove content.");
            }
        } else {
            result = new Result("File not found: " + targetFileName);
        }
        return result;
    }
}
