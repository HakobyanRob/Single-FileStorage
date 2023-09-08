# Single-File Storage Management System

This is a Java-based application that allows users to manage a single-file storage system. It provides functionalities for creating, retrieving, updating, and deleting files within the storage.

## Table of Contents

- **Prerequisites**
- **Getting Started**
- **Usage**


## Prerequisites

Before you can run the Single-File Storage Management System, make sure you have the following prerequisites installed:

- Java Development Kit (JDK) 16 or later
- Maven 3.6.3 or later

## Getting Started

1. Clone the repository to your local machine:


2. Navigate to the project directory:


3. Compile the project using Maven:

   ```
   mvn clean install
   ```

# Usage

The `StorageDefinitionManager` and `StorageManipulationManager` interfaces provide a set of methods for managing file storage and performing file operations. Here's a summary of the public methods and their return values:

## StorageDefinitionManager Interface

### `ResultDTO<File> createStorage()`

- Description: Creates a new storage at the file path provided by StoragePropertiesManager
- Return Value: A `ResultDTO` containing the created storage file if successful, or an error message if the operation fails.

### `Result deleteStorage()`

- Description: Deletes the storage.
- Return Value: A `Result` indicating the result of the deletion operation. A successful deletion will have no error message.

### `ResultDTO<File> getStorage()`

- Description: Retrieves the storage.
- Return Value: A `ResultDTO` containing the retrieved storage file if successful, or an error message if the operation fails.

## StorageManipulationManager Interface

### `ResultDTO<File> addFile(File file)`

- Description: Adds a file to the storage.
- Parameters:
   - `file` (Type: `File`) - The file to be added.
- Return Value: A `ResultDTO` containing the added file if successful, or an error message if the operation fails.

### `ResultDTO<byte[]> getFile(String fileName)`

- Description: Retrieves a file from the storage based on its name.
- Parameters:
   - `fileName` (Type: `String`) - The name of the file to retrieve.
- Return Value: A `ResultDTO` containing the file's content as a byte array if successful, or an error message if the operation fails.

### `ResultDTO<File> updateFile(File file)`

- Description: Updates an existing file in the storage.
- Parameters:
   - `file` (Type: `File`) - The updated file.
- Return Value: A `ResultDTO` containing the updated file if successful, or an error message if the operation fails.

### `Result deleteFile(String fileName)`

- Description: Deletes a file from the storage based on its name.
- Parameters:
   - `fileName` (Type: `String`) - The name of the file to delete.
- Return Value: A `Result` indicating the result of the deletion operation. A successful deletion will have no error message.

These interfaces and methods provide a structured way to interact with storage and files, allowing you to manage and manipulate files in your application while handling errors and success scenarios gracefully.

## StoragePropertiesManager Class

The `StoragePropertiesManager` class is responsible for managing storage-related properties, such as storage paths and file locations. It reads these properties from a configuration file, providing defaults if the file is missing or malformed.

### Constructor

#### `StoragePropertiesManager(String storagePropertiesPath)`

- Initializes the `StoragePropertiesManager` with the path to the storage properties configuration file.

    - `storagePropertiesPath`: The path to the configuration file.

### Public Methods

#### `String getStoragePath()`

- If the storage path is missing in the configuration file or if the file is not provided, the default storage path `"storage.txt"` will be used.

#### `String getTempPath()`

- If the temporary file path is missing in the configuration file or if the file is not provided, the default temporary file path `"temp/temp.txt"` will be used.

#### `String getBackupPath()`

- If the backup file path is missing in the configuration file or if the file is not provided, the default backup file path `"temp/backup.txt"` will be used.

By using default values in these cases, the `StoragePropertiesManager` class ensures that the application can continue to operate even when specific configuration details are not available or incomplete.

___

For more details about the implementation visit the documentation page.
