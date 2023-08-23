package com.github.hakobyanrob.result;

public record CRUDManagerResult(boolean successful, String error) {

    public boolean isSuccessful() {
        return successful;
    }

    public String getError() {
        return error;
    }
}
