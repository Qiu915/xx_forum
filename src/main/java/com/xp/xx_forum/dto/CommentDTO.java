package com.xp.xx_forum.dto;

import com.xp.xx_forum.bean.User;
import lombok.Data;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/8 16:13
 */
@Data
public class CommentDTO {
    private Long id;

    private Long parentId;

    private Integer type;

    private Long creator;

    private Integer commentCount;

    private String content;

    private Long gmtCreate;

    private Long gmtModified;

    private Integer likeCount;

    private User user;
}
