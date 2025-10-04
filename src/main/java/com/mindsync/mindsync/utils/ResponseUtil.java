package com.mindsync.mindsync.utils;

import com.mindsync.mindsync.dto.response.CommonResponse;

public class ResponseUtil {
    public static <T> CommonResponse<T> SUCCESS (String message, T data) {
        return new CommonResponse(ResponseStatus.SUCCESS, message, data);
    }

    public static <T> CommonResponse<T> FAILURE (String message, T data) {
        return new CommonResponse(ResponseStatus.FAILURE, message, data);
    }

    public static <T> CommonResponse<T> ERROR (String message, T data) {
        return new CommonResponse(ResponseStatus.ERROR, message, data);
    }
}
