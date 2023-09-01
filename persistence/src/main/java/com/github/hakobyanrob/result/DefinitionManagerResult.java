package com.github.hakobyanrob.result;

import java.io.File;

public record DefinitionManagerResult(boolean successful, String error, File storage) {

    public boolean isSuccessful() {
        return successful;
    }

    public String getError() {
        return error;
    }

    public File getStorage() {
        return storage;
    }
}
