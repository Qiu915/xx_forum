package com.xp.xx_forum.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/22 16:06
 */
@Data
public class Notice implements Serializable {

    private Long userId;
    private String name;
    private Long questionId;
    private String title;
    private Long gmtCreate;
}
