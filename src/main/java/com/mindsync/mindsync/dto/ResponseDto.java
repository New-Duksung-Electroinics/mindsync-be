package com.mindsync.mindsync.dto;

import com.mindsync.mindsync.utils.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private final ResponseStatus status;
    private final String message;
    private final T data;
}