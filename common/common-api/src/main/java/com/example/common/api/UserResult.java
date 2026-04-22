package com.example.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;
    private Boolean success;
    private String requestId;

    public boolean isSuccess() {
        return success != null && success;
    }

    public static <T> UserResult<T> ok(T data) {
        UserResult<T> result = new UserResult<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setSuccess(true);
        result.setRequestId(java.util.UUID.randomUUID().toString());
        return result;
    }

    public static <T> UserResult<T> fail(String message) {
        UserResult<T> result = new UserResult<>();
        result.setCode(500);
        result.setMessage(message);
        result.setSuccess(false);
        result.setRequestId(java.util.UUID.randomUUID().toString());
        return result;
    }
}
