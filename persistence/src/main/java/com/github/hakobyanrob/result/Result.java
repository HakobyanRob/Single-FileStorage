package com.github.hakobyanrob.result;

import java.util.Objects;

public class Result {
    private final String errorMessage;

    public Result() {
        errorMessage = null;
    }

    public Result(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessful() {
        return errorMessage == null;
    }

    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Result) obj;
        return Objects.equals(this.errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage);
    }

    @Override
    public String toString() {
        return "Result[" +
                "errorMessage=" + errorMessage + ']';
    }

}
