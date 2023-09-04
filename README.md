# Single-File Storage Management System

This is a Java-based application that allows users to manage a single-file storage system. It provides functionalities for creating, retrieving, updating, and deleting files within the storage.

## Table of Contents

- **Features**
- **Prerequisites**
- **Getting Started**
- **Usage**
- **Code Structure**
- **Implementation Details**

## Features

### Storage Manipulation Methods
- **Add File**: Add a file to the storage system.
- **Retrieve File**: Retrieve a file from the storage system.
- **Update File**: Update a file in the storage system.
- **Delete File**: Delete a file from the storage system.

### Storage Definition Methods
- **Create Storage**: Create a new single-file storage system.
- **Retrieve Storage**: Retrieve the existing single-file storage.
- **Delete Storage**: Delete the existing single-file storage.

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

4. Run the application:

   ```
   java -jar ./app/target/app-1.0-SNAPSHOT.jar
   ```

## Usage

1. When you run the application, you will be presented with a menu of options:

   ```
   Select an option:
   1. Add File
   2. Get File
   3. Update File
   4. Delete File
   5. Exit
   Enter your choice:
   ```

2. Choose an option by entering the corresponding number:

   - **Add File**: You can add a file to the storage system by providing the file's path.
   - **Get File**: Retrieve a file from the storage system by providing its name.
   - **Update File**: Update an existing file in the storage system by providing the file's path.
   - **Delete File**: Delete a file from the storage system by providing its name.
   - **Exit**: Exit the application.
   
## Code Structure

Here are some key details about the project:

- **Storage Properties**: The project uses a `persistence/src/main/resources/storage.properties` file to determine the storage path. You can customize this path by editing the properties file.

- **Storage Definition**: The `StorageDefinitionManager` interface and the `SingleFileStorageDefinitionManager` class in the `persistence` module define how storage is created, deleted, and retrieved.

- **Storage Manipulation**: The `StorageManipulationManager` interface and the `SingleFileStorageManipulationManager` class in the `persistence` module handle file manipulation operations such as adding, retrieving, updating, and deleting files within the storage.

## Implementation Details
### How the Single-FileSystem Works

- **Add File** When adding a file to the storage, the file's contents are encoded in Base64 format. This approach ensures that both binary data and text in multiple languages and character sets are safely stored within the storage. Alongside the encoded content, metadata such as the filename and the length of the Base64-encoded data are stored. This metadata simplifies future retrieval and ensures that each file is uniquely identifiable within the storage.

Example:
- File Name: `text.txt`
- File Content: `helloWorld`

Stored Data: `25|text.txt|aGVsbG9Xb3JsZA==`
- **Get File** is given a file name to find the file in the storage. The storage is traversed over effectively using the already known length of the files making it easy to avoid name collisions and faster than reading each byte separately.
- **Delete File** works similar to ```Get File``` to find the file to remove from the storage. It copies all the data that should be kept in a temporary file and replaces the original storage with the temporary storage. It ensures data corruption will not happen by using a backup file to fall back to in case of any exceptions. 
- **Update File** is just a combination of ```Delete File``` and ```Add File``` after.

- **Exceptions** are handled and instead a Result objects are returned describing the results of the operation.
- **Multithreading** is handled using read and write locks which make sure that multiple reads can happen at the same time, but writes have exclusive access to the storage. 