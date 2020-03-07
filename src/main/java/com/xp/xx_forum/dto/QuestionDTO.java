package com.xp.xx_forum.dto;

import com.xp.xx_forum.bean.User;
import lombok.Data;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/1 16:30
 */
@Data
public class QuestionDTO {
    private Integer id;

    private String title;

    private String description;

    private String tag;

    private Long creator;

    private Integer commentCount;

    private Long gmtCreate;

    private Long gmtModified;

    private Integer viewCount;

    private Integer likeCount;

    private User user;

}
