package com.staffing.exception;

import lombok.Getter;

/**
 * @author JngKang
 * @date 2022-03-01 08:16
 * @description 自定义异常
 */
@Getter
public class ServiceException extends RuntimeException {

    private String code;

    public ServiceException() {
    }

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
