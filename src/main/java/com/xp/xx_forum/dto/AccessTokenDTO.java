package com.xp.xx_forum.dto;

import lombok.Data;

/**
 * @author ph
 * @version 1.0
 * @date 2020/2/29 11:32
 */
@Data
public class AccessTokenDTO {

    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
