package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.result.ResultDTO;

import java.io.File;

/**
 * The StorageDefinitionManager interface defines methods for managing storage operations.
 * It provides functionality to create and delete storage resources.
 */
public interface StorageDefinitionManager {

    /**
     * Creates a new storage at the file path specified in storage.properties.
     *
     * @return A ResultDTO indicating the result of the operation.
     */
    ResultDTO<File> createStorage();

    /**
     * Deletes the storage.
     *
     * @return A Result indicating the result of the operation.
     */
    Result deleteStorage();

    /**
     * Retrieves the storage.
     *
     * @return A ResultDTO indicating the result of the operation.
     */
    ResultDTO<File> getStorage();
}
