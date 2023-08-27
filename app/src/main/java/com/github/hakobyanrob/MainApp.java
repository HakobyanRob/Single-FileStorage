package com.github.hakobyanrob;

import com.github.hakobyanrob.result.ManipulationManagerResult;
import com.github.hakobyanrob.services.common.StorageFilePathManager;
import com.github.hakobyanrob.services.file.SingleFileStorageManipulationManager;
import com.github.hakobyanrob.services.singlefilestorage.SingleFileStorageDefinitionManager;

import java.io.File;

public class MainApp {

    public static void main(String[] args) {
        String defaultStoragePropertiesPath = "persistence/src/main/resources/storage.properties";
        String storagePropertiesPath = args.length > 0 ? args[0] : defaultStoragePropertiesPath;

        StorageFilePathManager storageFilePathManager = new StorageFilePathManager(storagePropertiesPath);
        storageFilePathManager.readStorageProperties();
        var storageDefinitionManager = new SingleFileStorageDefinitionManager(storageFilePathManager.getStoragePath());

        storageDefinitionManager.createStorage();
        var manager = new SingleFileStorageManipulationManager(storageDefinitionManager);
        File file = new File("C:\\Users\\hakob\\Desktop\\workspace\\java\\Single-FileStorage\\persistence\\src\\test\\resources\\text.txt");
        ManipulationManagerResult manipulationManagerResult = manager.addFile(file);
        System.out.println(manipulationManagerResult.error());
        file = new File("C:\\Users\\hakob\\Desktop\\workspace\\java\\Single-FileStorage\\persistence\\src\\test\\resources\\json.json");
        manipulationManagerResult = manager.addFile(file);
        System.out.println(manipulationManagerResult.error());
        manager.deleteFile(file.getName());
    }

}
