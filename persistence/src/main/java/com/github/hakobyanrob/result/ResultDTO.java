package com.github.hakobyanrob.result;

public final class ResultDTO<T> extends Result {

    private T dto;

    public ResultDTO(T dto) {
        this.dto = dto;
    }

    public ResultDTO(String error) {
        super(error);
    }

    public T getDto() {
        return dto;
    }
}
