package com.example.bootdemo.tools.Exception;

/**
 * Author: YANG
 * Date: 2023/1/29 9:19
 * Describe:
 */
public class CommonException  extends Exception {
    private static final long serialVersionUID = 1L;
    private static final int SystemErrorCode = 999;   //系统错误
    private static final int BusinessErrorCode = 99;  //逻辑错误

    private int errorCode;  //错误编码

    public CommonException(int errorCode, String message) {
        super(message);
        this.setErrorCode(errorCode);
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.setErrorCode(errorCode);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
