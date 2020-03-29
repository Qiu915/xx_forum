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
    USER_NOT_FOUND(1004,"用户没有找到"),
    NO_LOGIN(1005,"当前未登录，请先登录再操作"),
    CONTENT_NOT_NULL(1006,"评论的内容不能为空或空字符串"),
    TARGET_NOT_FOUND(1007,"未选中问题或题目进行评论"),
    COMMENT_NOT_FOUND(1008,"你要找的评论不存在了"),

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
