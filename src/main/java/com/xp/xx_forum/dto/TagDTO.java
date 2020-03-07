package com.xp.xx_forum.dto;

import lombok.Data;

import java.util.List;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/5 14:13
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
