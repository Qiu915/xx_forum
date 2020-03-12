package com.xp.xx_forum.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/8 14:10
 */
public enum CommentType {
    QUESTION(0),
    COMMENT(1);
    private Integer type;

    CommentType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

}
