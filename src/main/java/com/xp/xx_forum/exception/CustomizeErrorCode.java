package com.xp.xx_forum.exception;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/5 19:19
 */
public enum CustomizeErrorCode {
    USER_NOT_EXIT(1000,"用户不存在"),
    QUESTION_NOT_FOUND(1001,"你要找的问题不存在"),
    INVALID_OPERATION(1002,"当前操作无效"),
    SYS_ERROR(1003,"系统出现问题了，请稍后再试"),
    USER_NOT_FOUND(1004,"用户没有找到")
    ;

    private Integer Code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        Code = code;
        this.message = message;
    }

    public Integer getCode() {
        return Code;
    }

    public String getMessage() {
        return message;
    }
}
