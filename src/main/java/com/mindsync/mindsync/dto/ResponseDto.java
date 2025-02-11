package com.mindsync.mindsync.dto;

import com.mindsync.mindsync.utils.ResponseStatus;
import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private final ResponseStatus status;
    private final String message;
    private final T data;

    public ResponseDto(ResponseStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}