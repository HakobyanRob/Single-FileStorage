package com.github.hakobyanrob;

import com.github.hakobyanrob.result.CRUDManagerResult;
import com.github.hakobyanrob.services.file.SingleFileStorageFileCRUDManager;
import com.github.hakobyanrob.services.singlefilestorage.StorageManager;
import com.github.hakobyanrob.services.singlefilestorage.SingleFileStorageManager;

import java.io.File;
import java.io.IOException;


public class MainApp {

    public static void main(String[] args) throws IOException {
        String filePath = "path_to_your_system_file.txt";

        // Create the single-file system service
        StorageManager fileService = new SingleFileStorageManager();
        fileService.createStorage(filePath);

        SingleFileStorageFileCRUDManager manager = new SingleFileStorageFileCRUDManager(fileService);
        File file = new File("C:\\Users\\hakob\\Desktop\\workspace\\java\\Single-FileStorage\\example.txt");
        manager.addFile(file);
        file = new File("C:\\Users\\hakob\\Desktop\\workspace\\java\\Single-FileStorage\\example2.txt");
        manager.addFile(file);

        file = new File("C:\\Users\\hakob\\Desktop\\workspace\\java\\Single-FileStorage\\front_metahuman_test1.png");
        manager.addFile(file);
        CRUDManagerResult result = manager.getFile("front_metahuman_test9071.png");
        if (result.isSuccessful()) {
            result.getFile().createNewFile();
        }

    }
}
