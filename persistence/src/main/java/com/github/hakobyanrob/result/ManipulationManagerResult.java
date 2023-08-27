package com.github.hakobyanrob.result;

import java.io.File;

public record ManipulationManagerResult(boolean successful, String error, File file) {

    public boolean isSuccessful() {
        return successful;
    }

    public String getError() {
        return error;
    }

    public File getFile() {
        return file;
    }
}
