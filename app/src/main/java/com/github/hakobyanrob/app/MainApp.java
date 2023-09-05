package com.github.hakobyanrob.app;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.result.ResultDTO;
import com.github.hakobyanrob.services.common.StoragePropertiesManager;
import com.github.hakobyanrob.services.storageDefinition.SingleFileStorageDefinitionManager;
import com.github.hakobyanrob.services.storageDefinition.StorageDefinitionManager;
import com.github.hakobyanrob.services.storageManipulation.SingleFileStorageManipulationManager;
import com.github.hakobyanrob.services.storageManipulation.StorageManipulationManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        StorageManipulationManager manager = init(args);
        userInputHandler(manager);
    }

    private static StorageManipulationManager init(String[] args) {
        String defaultStoragePropertiesPath = "persistence/src/main/resources/storage.properties";
        String storagePropertiesPath = args.length > 0 ? args[0] : defaultStoragePropertiesPath;

        StoragePropertiesManager storagePropertiesManager = new StoragePropertiesManager(storagePropertiesPath);
        StorageDefinitionManager storageDefinitionManager = new SingleFileStorageDefinitionManager(storagePropertiesManager.getStoragePath());
        if (!storageDefinitionManager.createStorage().isSuccessful()) {
            System.err.println("Failed to create Storage...");
            System.exit(0);
        }

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

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.err.println("Invalid input");
                continue;
            } finally {
                scanner.nextLine();
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter file path: ");
                    String filePath = scanner.nextLine();
                    File fileToAdd = new File(filePath);
                    ResultDTO<File> addResult = manager.addFile(fileToAdd);
                    if (addResult.isSuccessful()) {
                        System.out.println("Successfully added file");
                    } else {
                        System.err.println(addResult.getErrorMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Enter file name: ");
                    String fileNameToGet = scanner.nextLine();
                    ResultDTO<byte[]> getResult = manager.getFile(fileNameToGet);
                    if (getResult.isSuccessful()) {
                        System.out.println("Successfully got file. " +
                                "\nPlease specify a file path to save the results, or press Enter to save it to the original path:");
                        String filePath = scanner.nextLine();

                        if (filePath.isEmpty()) {
                            filePath = fileNameToGet;
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream(filePath);

                            fos.write(getResult.getDto());
                            fos.close();

                            System.out.println("File saved successfully at: " + filePath);
                        } catch (IOException e) {
                            System.err.println("Error saving file: " + e.getMessage());
                        }
                    } else {
                        System.err.println(getResult.getErrorMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter file path: ");
                    String filePathToUpdate = scanner.nextLine();
                    File fileToUpdate = new File(filePathToUpdate);
                    ResultDTO<File> updateResult = manager.updateFile(fileToUpdate);
                    if (updateResult.isSuccessful()) {
                        System.out.println("Successfully updated file");
                    } else {
                        System.err.println(updateResult.getErrorMessage());
                    }
                }
                case 4 -> {
                    System.out.print("Enter file name: ");
                    String fileNameToDelete = scanner.nextLine();
                    Result deleteResult = manager.deleteFile(fileNameToDelete);
                    if (deleteResult.isSuccessful()) {
                        System.out.println("Successfully deleted file");
                    } else {
                        System.err.println(deleteResult.getErrorMessage());
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
