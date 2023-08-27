package com.github.hakobyanrob.services.file;

import com.github.hakobyanrob.result.ManipulationManagerResult;

import java.io.File;

public interface StorageManipulationManager {

    ManipulationManagerResult addFile(File file);

    ManipulationManagerResult getFile(String filename);

    ManipulationManagerResult updateFile(File file);

    ManipulationManagerResult deleteFile(String filename);
}
