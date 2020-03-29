package com.xp.xx_forum.enums;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/14 14:47
 */
public enum LikedType {
    QUESTION(0),
    COMMENT(1);
    private Integer type;

    LikedType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
