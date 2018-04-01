package com.warthur.jwt.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private Integer status = 0;
    private String message = "请求成功";
    private T content ;

    private Response(Error errorCode) {
        this.status = errorCode.getCode();
        this.message = errorCode.getMsg();
    }

    public Response(T data) {
        this(Error.REQUEST_SUCCESS);
        this.content = data;
    }

    public Response(Error res, T data) {
        this(res.getCode(), res.getMsg(), data);
    }

    public Response(int code, String message) {
        this(code, message, null);
    }

    public static Response error(String message) {
        return new Response(1, message);
    }

    public static Response error(int code,String message) {
        return new Response(code,message);
    }
}
