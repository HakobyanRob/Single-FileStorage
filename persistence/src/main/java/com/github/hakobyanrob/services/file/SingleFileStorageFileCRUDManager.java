package com.github.hakobyanrob.services.file;

import com.github.hakobyanrob.result.CRUDManagerResult;
import com.github.hakobyanrob.result.StorageManagerResult;
import com.github.hakobyanrob.services.singlefilestorage.StorageManager;

import java.io.BufferedReader;
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

public class SingleFileStorageFileCRUDManager implements FileCRUDManager {

    private static final Logger logger = Logger.getLogger(SingleFileStorageFileCRUDManager.class.getName());

    private final StorageManager storageManager;

    public SingleFileStorageFileCRUDManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public CRUDManagerResult addFile(File file) {
        // Validate file
        if (file == null || !file.exists()) {
            return new CRUDManagerResult(false, "Invalid file object", null);
        }

        StorageManagerResult managerResult = storageManager.getStorage();
        if (!managerResult.isSuccessful()) {
            logger.log(Level.WARNING, "Storage not found. Creating new storage.");
            String filePath = "storage_file.txt";
            StorageManagerResult creationResult = storageManager.createStorage(filePath);
            if (creationResult.isSuccessful()) {
                logger.log(Level.INFO, "Successfully created new storage: " + filePath);
            } else {
                logger.log(Level.SEVERE, "Failed to create new storage.");
                return new CRUDManagerResult(false, "Failed to create new storage", null);
            }
        }
        try (FileWriter writer = new FileWriter(managerResult.getStorage(), true)) {
            // Create metadata JSON
            String metadata = createMetadata(file);
            // Append separator and metadata to the system file
            writer.write(metadata);

            logger.log(Level.INFO, "File added: " + file.getName());
            return new CRUDManagerResult(true, null, file);
        } catch (IOException e) {
            String message = "Error creating file: " + file.getName();
            logger.log(Level.SEVERE, message, e);
            return new CRUDManagerResult(false, message, null);
        }
    }

    @Override
    public CRUDManagerResult getFile(String filename) {
        StorageManagerResult managerResult = storageManager.getStorage();
        if (!managerResult.isSuccessful()) {
            String error = "Failed to get storage.";
            logger.log(Level.SEVERE, error);
            return new CRUDManagerResult(false, error, null);
        }
        File storage = managerResult.getStorage();
        try {
            File file = findFile(storage, filename);
            if (file != null) {
                logger.log(Level.INFO, "Successfully found the file: " + filename);
                return new CRUDManagerResult(true, null, file);
            } else {
                String error = "Failed to find file: " + filename;
                logger.log(Level.WARNING, error);
                return new CRUDManagerResult(false, error, null);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to find the file: " + e);
            return new CRUDManagerResult(false, e.getMessage(), null);
        }
    }

    @Override
    public CRUDManagerResult updateFile(File file) {
        return null;
    }

    @Override
    public CRUDManagerResult deleteFile(String filename) {
        return null;
    }

    private String createMetadata(File file) throws IOException {
        Path filePath = file.toPath();

        String encodedFile = encodeFile(filePath);

        // We can easily add file creation time and other attributes to the saved data using the following method:
        // BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
        String metadata = String.format(
                "fileName#%s|Contents#%s",
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

    private static File convertByteArrayToFile(String filePath, byte[] byteArray) throws IOException {
        File virtualFile = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(virtualFile);
        outputStream.write(byteArray);
        outputStream.close();
        return virtualFile;
    }

    private File findFile(File storage, String targetFileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(storage));
        String line;
        while ((line = reader.readLine()) != null) {
            int length = Integer.parseInt(line.substring(0, line.indexOf("|")));
            line = line.substring(line.indexOf("|") + 1); // Skip the length and move to the next section

            String content = line.substring(line.indexOf("fileName#") + 9, line.indexOf("|", line.indexOf("fileName#")));
            while (!content.equals(targetFileName) && length < line.length()) {
                line = line.substring(length);
                length = Integer.parseInt(line.substring(0, line.indexOf("|")));
                line = line.substring(line.indexOf("|") + 1);
                content = line.substring(line.indexOf("fileName#") + 9, line.indexOf("|", line.indexOf("fileName#")));
            }
            if (content.equals(targetFileName)) {
                String encodedContents = line.substring(19 + targetFileName.length(), length);
                return decodeFile(targetFileName + "encoded", encodedContents);
            }
        }
        return null;
    }
}
