package com.mindsync.mindsync.dto.response;

import com.mindsync.mindsync.utils.ResponseStatus;
import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private final ResponseStatus status;
    private final String message;
    private final T data;

    public CommonResponse(ResponseStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}