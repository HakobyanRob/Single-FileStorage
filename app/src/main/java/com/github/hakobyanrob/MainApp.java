package com.github.hakobyanrob;

import com.github.hakobyanrob.result.ManipulationManagerResult;
import com.github.hakobyanrob.services.common.StorageFilePathManager;
import com.github.hakobyanrob.services.storageManipulation.SingleFileStorageManipulationManager;
import com.github.hakobyanrob.services.storageManipulation.StorageManipulationManager;
import com.github.hakobyanrob.services.storageDefinition.SingleFileStorageDefinitionManager;

import java.io.File;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        StorageManipulationManager manager = init(args);
        userInputHandler(manager);
    }

    private static StorageManipulationManager init(String[] args) {
        String defaultStoragePropertiesPath = "persistence/src/main/resources/storage.properties";
        String storagePropertiesPath = args.length > 0 ? args[0] : defaultStoragePropertiesPath;

        StorageFilePathManager storageFilePathManager = new StorageFilePathManager(storagePropertiesPath);
        storageFilePathManager.readStorageProperties();
        var storageDefinitionManager = new SingleFileStorageDefinitionManager(storageFilePathManager.getStoragePath());
        storageDefinitionManager.createStorage();

        return new SingleFileStorageManipulationManager(storageDefinitionManager);
    }

    private static void userInputHandler(StorageManipulationManager manager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select an option:");
            System.out.println("1. Add File");
            System.out.println("2. Get File");
            System.out.println("3. Update File");
            System.out.println("4. Delete File");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter file path: ");
                    String filePath = scanner.nextLine();
                    File fileToAdd = new File(filePath);
                    ManipulationManagerResult addResult = manager.addFile(fileToAdd);
                    if (addResult.isSuccessful()) {
                        System.out.println("Successfully added file");
                    } else {
                        System.err.println(addResult.getError());
                    }
                }
                case 2 -> {
                    System.out.print("Enter file name: ");
                    String fileNameToGet = scanner.nextLine();
                    ManipulationManagerResult getResult = manager.getFile(fileNameToGet);
                    if (getResult.isSuccessful()) {
                        System.out.println("Successfully got file");
                    } else {
                        System.err.println(getResult.getError());
                    }
                }
                case 3 -> {
                    System.out.print("Enter file path: ");
                    String filePathToUpdate = scanner.nextLine();
                    File fileToUpdate = new File(filePathToUpdate);
                    ManipulationManagerResult updateResult = manager.updateFile(fileToUpdate);
                    if (updateResult.isSuccessful()) {
                        System.out.println("Successfully updated file");
                    } else {
                        System.err.println(updateResult.getError());
                    }
                }
                case 4 -> {
                    System.out.print("Enter file name: ");
                    String fileNameToDelete = scanner.nextLine();
                    ManipulationManagerResult deleteResult = manager.deleteFile(fileNameToDelete);
                    if (deleteResult.isSuccessful()) {
                        System.out.println("Successfully deleted file");
                    } else {
                        System.err.println(deleteResult.getError());
                    }
                }
                case 5 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
}
