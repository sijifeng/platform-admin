package com.season.platform.web.site.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by jiyc on 2017/2/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JsonResponseEntity<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public JsonResponseEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResponseEntity(){}

    public JsonResponseEntity(T content) {
        this.code = SUCCESS_CODE;
        this.data = content;
    }


    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;
    public static final JsonResponseEntity<String> SUCCESS = new JsonResponseEntity<>(null);
    public static final JsonResponseEntity<String> FAIL = new JsonResponseEntity<>(FAIL_CODE, null);


    private int recordsTotal;
    private int recordsFiltered;
}

