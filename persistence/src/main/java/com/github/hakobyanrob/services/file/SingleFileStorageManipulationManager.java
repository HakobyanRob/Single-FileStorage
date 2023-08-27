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
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleFileStorageManipulationManager implements StorageManipulationManager {

    private static final Logger logger = Logger.getLogger(SingleFileStorageManipulationManager.class.getName());

    private final StorageDefinitionManager storageDefinitionManager;

    public SingleFileStorageManipulationManager(StorageDefinitionManager storageDefinitionManager) {
        this.storageDefinitionManager = storageDefinitionManager;
    }

    @Override
    public ManipulationManagerResult addFile(File file) {
        if (file == null || !file.exists()) {
            return new ManipulationManagerResult(false, "Invalid file object", null);
        }

        StorageManagerResult managerResult = storageDefinitionManager.getStorage();
        if (!managerResult.isSuccessful()) {
            logger.log(Level.WARNING, "Storage not found. Creating new storage.");
            StorageManagerResult creationResult = storageDefinitionManager.createStorage();
            if (creationResult.isSuccessful()) {
                logger.log(Level.INFO, "Successfully created new storage");
            } else {
                logger.log(Level.SEVERE, "Failed to create new storage.");
                return new ManipulationManagerResult(false, "Failed to create new storage", null);
            }
        }
        if (getFile(file.getName()).isSuccessful()) {
            return new ManipulationManagerResult(false,
                    String.format("File with this name %s already exists", file.getName()), null);
        }
        try (FileWriter writer = new FileWriter(managerResult.getStorage(), true)) {
            String metadata = createMetadata(file);
            writer.write(metadata);

            logger.log(Level.INFO, "File added: " + file.getName());
            return new ManipulationManagerResult(true, null, file);
        } catch (IOException e) {
            String message = "Error creating file: " + file.getName();
            logger.log(Level.SEVERE, message, e);
            return new ManipulationManagerResult(false, message, null);
        }
    }

    @Override
    public ManipulationManagerResult getFile(String filename) {
        StorageManagerResult managerResult = storageDefinitionManager.getStorage();
        if (!managerResult.isSuccessful()) {
            String error = "Failed to get storage.";
            logger.log(Level.SEVERE, error);
            return new ManipulationManagerResult(false, error, null);
        }
        File storage = managerResult.getStorage();
        try {
            File file = findFile(storage, filename);
            if (file != null) {
                logger.log(Level.INFO, "Successfully found the file: " + filename);
                return new ManipulationManagerResult(true, null, file);
            } else {
                String error = "Failed to find file: " + filename;
                logger.log(Level.SEVERE, error);
                return new ManipulationManagerResult(false, error, null);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to find the file: " + e);
            return new ManipulationManagerResult(false, e.getMessage(), null);
        }
    }

    @Override
    public ManipulationManagerResult updateFile(File file) {
        ManipulationManagerResult manipulationManagerResult = deleteFile(file.getName());
        if (!manipulationManagerResult.isSuccessful()) {
            return manipulationManagerResult;
        }
        return addFile(file);
    }

    @Override
    public ManipulationManagerResult deleteFile(String filename) {
        StorageManagerResult managerResult = storageDefinitionManager.getStorage();
        if (!managerResult.isSuccessful()) {
            String error = "Failed to get storage.";
            logger.log(Level.SEVERE, error);
            return new ManipulationManagerResult(false, error, null);
        }
        File storage = managerResult.getStorage();

        try {
            deleteFile(storage, filename);
            return new ManipulationManagerResult(true, null, null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to delete the file: " + e);
            return new ManipulationManagerResult(false, e.getMessage(), null);
        }
    }

    private String createMetadata(File file) throws IOException {
        Path filePath = file.toPath();

        String encodedFile = encodeFile(filePath);

        // We can easily add file creation time and other attributes to the saved data using the following method:
        // BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
        String metadata = String.format(
                "fileName#%s|%s",
                file.getName(), encodedFile.replace("\"", "\\\""));
        long size = metadata.length();

        return size + "|" + metadata;
    }

    private String encodeFile(Path path) throws IOException {
        byte[] fileContent = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private File decodeFile(String fileName, String encodedFile) throws IOException {
        byte[] decode = Base64.getDecoder().decode(encodedFile);
        return convertByteArrayToFile(fileName, decode);
    }

    //The file will be deleted on exit as Java must create a physical file for the object.
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
                int length = Integer.parseInt(line.substring(0, line.indexOf("|")));
                line = line.substring(line.indexOf("|") + 1); // Skip the length and move to the next section

                String currentFileName = line.substring(line.indexOf("fileName#") + 9, line.indexOf("|", line.indexOf("fileName#")));
                while (!currentFileName.equals(targetFileName) && length < line.length()) {
                    line = line.substring(length);
                    length = Integer.parseInt(line.substring(0, line.indexOf("|")));
                    line = line.substring(line.indexOf("|") + 1);
                    currentFileName = line.substring(line.indexOf("fileName#") + 9, line.indexOf("|", line.indexOf("fileName#")));
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
                int length = Integer.parseInt(line.substring(0, line.indexOf("|")));
                line = line.substring(line.indexOf("|") + 1);
                String currentLine = length + "|" + line.substring(0, length);

                String content = line.substring(line.indexOf("fileName#") + 9, line.indexOf("|", line.indexOf("fileName#")));
                if (!content.equals(targetFileName)) {
                    writer.write(currentLine);
                }
            }
        }

        File backup = new File("backup.txt");
        backup.deleteOnExit();
        Files.copy(storage.toPath(), backup.toPath());

        if (storage.delete() && tempFile.renameTo(storage)) {
            System.out.println("Content removed successfully.");
        } else {
            System.err.println("Failed to remove content.");
            System.err.println("Reverting to back-up.");
            Files.copy(backup.toPath(), storage.toPath());
        }
    }
}
