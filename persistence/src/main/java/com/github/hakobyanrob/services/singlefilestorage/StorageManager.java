package com.github.hakobyanrob.services.singlefilestorage;

import com.github.hakobyanrob.result.StorageManagerResult;

/**
 * The StorageManager interface defines methods for managing storage operations.
 * It provides functionality to create and delete storage resources.
 */
public interface StorageManager {

    /**
     * Creates a new storage at the specified file path.
     *
     * @param filePath The path where the storage should be created.
     * @return A StorageManagerResult indicating the result of the operation.
     */
    StorageManagerResult createStorage(String filePath);

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
