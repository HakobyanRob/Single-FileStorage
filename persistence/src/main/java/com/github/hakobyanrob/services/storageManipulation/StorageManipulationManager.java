package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.result.ManipulationManagerResult;

import java.io.File;

public interface StorageManipulationManager {

    ManipulationManagerResult addFile(File file);

    ManipulationManagerResult getFile(String fileName);

    ManipulationManagerResult updateFile(File file);

    ManipulationManagerResult deleteFile(String fileName);
}
