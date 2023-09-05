package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.result.ResultDTO;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SingleFileStorageDefinitionManager class is responsible for managing the creation and deletion
 * of a single-file storage. It ensures that only one instance of the storage
 * is created, following the singleton design pattern.
 * Only one storage can be created at a time.
 */
public class SingleFileStorageDefinitionManager implements StorageDefinitionManager {
    private static final Logger logger = Logger.getLogger(SingleFileStorageDefinitionManager.class.getName());

    private File singleFileStorage;

    private final String storagePath;

    public SingleFileStorageDefinitionManager(String storagePath) {
        this.storagePath = storagePath;
    }

    /**
     * Creates a new storage at the file path specified in storage.properties.
     * If storage exists or a file with same name and extension exists return the already existing file.
     *
     * @return A ResultDTO indicating the result of the operation.
     * If the operation succeeds it will return a reference to the object.
     */
    @Override
    public ResultDTO<File> createStorage() {
        File file = new File(storagePath);
        if (singleFileStorage != null) {
            String message = "File Storage already created!";
            logger.log(Level.INFO, message);
            return new ResultDTO<>(singleFileStorage);
        }
        if (loadStorage() != null) {
            String message = "File Storage found at: " + storagePath + ". Not creating a new one";
            logger.log(Level.INFO, message);
            return new ResultDTO<>(singleFileStorage);
        }

        try {
            if (!file.createNewFile()) {
                String message = "Failed to create the file storage at path: " + storagePath;
                logger.log(Level.SEVERE, message);
                return new ResultDTO<>(message);
            }
            logger.log(Level.INFO, "File storage created successfully at path: " + storagePath);
            singleFileStorage = file;
            return new ResultDTO<>(singleFileStorage);
        } catch (IOException e) {
            String message = "Error creating the file storage at path: " + storagePath;
            logger.log(Level.SEVERE, message, e);
            return new ResultDTO<>(e.getMessage());
        }
    }

    /**
     * Deletes the single-file storage.
     * Does not delete the storage if it has not been loaded before to not delete is accidentally.
     *
     * @return A Result indicating the result of the operation.
     */
    @Override
    public Result deleteStorage() {
        if (singleFileStorage == null) {
            String message = "File Storage does not exist";
            logger.log(Level.WARNING, message);
            return new Result(message);
        }
        if (!singleFileStorage.delete()) {
            String message = "Failed to delete the file storage at path: " + singleFileStorage.getAbsolutePath();
            logger.log(Level.SEVERE, message);
            return new Result(message);
        }
        logger.log(Level.INFO, "File storage deleted at path: " + singleFileStorage);
        singleFileStorage = null;
        return new Result();
    }

    /**
     * Retrieves the single-file storage.
     * If storage has not been loaded before, tries to find the storage at the storage path.
     *
     * @return A ResultDTO indicating the result of the operation. If the storage exists, the result will
     * contain the reference to the storage file. If the storage does not exist, the result will indicate failure.
     */
    @Override
    public ResultDTO<File> getStorage() {
        if (singleFileStorage == null) {
            logger.log(Level.WARNING, "File Storage not found. Trying to load from path: " + storagePath);
            File loadedStorage = loadStorage();
            if (loadedStorage == null) {
                String message = "Failed to load Storage";
                logger.log(Level.SEVERE, message);
                return new ResultDTO<>(message);
            } else {
                logger.log(Level.INFO, "Successfully loaded storage at " + storagePath);
            }
        }
        return new ResultDTO<>(singleFileStorage);
    }

    /**
     * Loading storage from the storage path.
     *
     * @return the loaded storage or null if storage does not exist
     */
    private File loadStorage() {
        File storage = new File(storagePath);
        if (storage.exists()) {
            singleFileStorage = storage;
            return storage;
        } else {
            return null;
        }
    }
}
