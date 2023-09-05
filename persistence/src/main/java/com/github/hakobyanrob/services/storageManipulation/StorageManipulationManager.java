package com.github.hakobyanrob.services.storageManipulation;

import com.github.hakobyanrob.result.Result;
import com.github.hakobyanrob.result.ResultDTO;

import java.io.File;
import java.io.InputStream;

public interface StorageManipulationManager {

    ResultDTO<File> addFile(File file);

    ResultDTO<byte[]> getFile(String fileName);

    ResultDTO<File> updateFile(File file);

    Result deleteFile(String fileName);
}
