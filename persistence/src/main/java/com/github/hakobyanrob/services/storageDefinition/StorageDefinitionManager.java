package com.github.hakobyanrob.services.storageDefinition;

import com.github.hakobyanrob.result.DefinitionManagerResult;

/**
 * The StorageDefinitionManager interface defines methods for managing storage operations.
 * It provides functionality to create and delete storage resources.
 */
public interface StorageDefinitionManager {

    /**
     * Creates a new storage at the file path specified in storage.properties.
     *
     * @return A DefinitionManagerResult indicating the result of the operation.
     */
    DefinitionManagerResult createStorage();

    /**
     * Deletes the storage.
     *
     * @return A DefinitionManagerResult indicating the result of the operation.
     */
    DefinitionManagerResult deleteStorage();

    /**
     * Retrieves the storage.
     *
     * @return A DefinitionManagerResult indicating the result of the operation.
     */
    DefinitionManagerResult getStorage();
}
