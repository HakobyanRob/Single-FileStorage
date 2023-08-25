package com.github.hakobyanrob.services.file;

import com.github.hakobyanrob.result.CRUDManagerResult;

import java.io.File;

public interface FileCRUDManager {

    CRUDManagerResult addFile(File file);

    CRUDManagerResult getFile(String filename);

    CRUDManagerResult updateFile(File file);

    CRUDManagerResult deleteFile(String filename);
}
