package com.github.hakobyanrob.services.singlefilestorage;

import com.github.hakobyanrob.result.StorageManagerResult;

/**
 * The StorageDefinitionManager interface defines methods for managing storage operations.
 * It provides functionality to create and delete storage resources.
 */
public interface StorageDefinitionManager {

    /**
     * Creates a new storage at the file path specified in storage.properties.
     *
     * @return A StorageManagerResult indicating the result of the operation.
     */
    StorageManagerResult createStorage();

    /**
     * Deletes the storage.
     *
     * @return A StorageManagerResult indicating the result of the operation.
     */
    StorageManagerResult deleteStorage();

    /**
     * Retrieves the storage.
     *
     * @return A StorageManagerResult indicating the result of the operation.
     */
    StorageManagerResult getStorage();
}
