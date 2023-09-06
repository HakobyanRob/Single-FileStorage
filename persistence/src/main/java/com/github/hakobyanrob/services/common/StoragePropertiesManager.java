package com.github.hakobyanrob.services.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StoragePropertiesManager {
    private static final Logger logger = Logger.getLogger(StoragePropertiesManager.class.getName());

    private final String storagePropertiesPath;

    private static final String storagePathKey = "storagePath";
    private String storagePath;

    private static final String tempPathKey = "tempFile";
    private String tempPath;

    private static final String backupPathKey = "backupFile";
    private String backupPath;


    public StoragePropertiesManager(String storagePropertiesPath) {
        this.storagePropertiesPath = storagePropertiesPath;
        initializeProperties();
    }

    private void initializeProperties() {
        Properties prop = new Properties();
        String defaultStoragePath = "storage.txt";
        String defaultTempPath = "temp/temp.txt";
        String defaultBackupPath = "temp/backup.txt";
        try (FileInputStream inputStream = new FileInputStream(storagePropertiesPath)) {
            prop.load(inputStream);
            storagePath = prop.getProperty(storagePathKey, defaultStoragePath);
            tempPath = prop.getProperty(tempPathKey, defaultTempPath);
            backupPath = prop.getProperty(backupPathKey, defaultBackupPath);
        } catch (IOException | NumberFormatException e) {
            logger.log(Level.WARNING, "Failed to read storage properties from file "
                    + storagePropertiesPath + ". Default values will be set", e);
            storagePath = defaultStoragePath;
            tempPath = defaultTempPath;
            backupPath = defaultBackupPath;
        }
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getTempPath() {
        return tempPath;
    }

    public String getBackupPath() {
        return backupPath;
    }
}
