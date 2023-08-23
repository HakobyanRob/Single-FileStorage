package com.github.hakobyanrob.services.singlefilestorage;

import com.github.hakobyanrob.result.StorageManagerResult;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SingleFileStorageManager class is responsible for managing the creation and deletion
 * of a single-file storage. It ensures that only one instance of the storage
 * is created, following the singleton design pattern.
 * Only one storage can be created at a time.
 */
public class SingleFileStorageManager implements StorageManager {

    private static final Logger logger = Logger.getLogger(SingleFileStorageManager.class.getName());

    private File singleFileStorage;

    public SingleFileStorageManager() {
    }

    /**
     * Creates a single-file storage at the specified file path.
     *
     * @param filePath The path where the single-file storage should be created.
     * @return A StorageManagerResult indicating the result of the operation.
     */
    @Override
    public StorageManagerResult createStorage(String filePath) {
        File file = new File(filePath);

        if (singleFileStorage != null && singleFileStorage.getAbsolutePath().equals(file.getAbsolutePath())) {
            String message = "File Storage already exists located at: " + filePath;
            logger.log(Level.WARNING, message);
            return new StorageManagerResult(false, message, null);
        }
        if (file.exists()) {
            String message = "File with same name already exists at path: " + filePath;
            logger.log(Level.WARNING, message);
            return new StorageManagerResult(false, message, null);
        }

        try {
            if (!file.createNewFile()) {
                String message = "Failed to create the file storage at path: " + filePath;
                logger.log(Level.SEVERE, message);
                return new StorageManagerResult(false, message, null);
            }
            logger.log(Level.INFO, "File storage created successfully at path: " + filePath);
            singleFileStorage = file;
            return new StorageManagerResult(true, null, singleFileStorage);
        } catch (IOException e) {
            String message = "Error creating the file storage at path: " + filePath;
            logger.log(Level.SEVERE, message, e);
            return new StorageManagerResult(false, e.getMessage(), null);
        }
    }

    /**
     * Deletes the single-file storage.
     *
     * @return A StorageManagerResult indicating the result of the operation.
     */
    @Override
    public StorageManagerResult deleteStorage() {
        if (singleFileStorage == null) {
            String message = "File Storage does not exist";
            logger.log(Level.WARNING, message);
            return new StorageManagerResult(false, message, null);
        }
        if (!singleFileStorage.delete()) {
            String message = "Failed to delete the file storage at path: " + singleFileStorage.getAbsolutePath();
            logger.log(Level.SEVERE, message);
            return new StorageManagerResult(false, message, null);
        }
        logger.log(Level.INFO, "File storage deleted at path: " + singleFileStorage);
        singleFileStorage = null;
        return new StorageManagerResult(true, null, null);
    }

    /**
     * Retrieves the single-file storage.
     *
     * @return A StorageManagerResult indicating the result of the operation. If the storage exists, the result will
     * contain the reference to the storage file. If the storage does not exist, the result will indicate failure.
     */
    @Override
    public StorageManagerResult getStorage() {
        if (singleFileStorage == null) {
            String message = "File Storage does not exist";
            logger.log(Level.WARNING, message);
            return new StorageManagerResult(false, message, null);
        }
        return new StorageManagerResult(true, null, singleFileStorage);
    }
}
