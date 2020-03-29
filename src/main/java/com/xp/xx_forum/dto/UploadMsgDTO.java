package com.xp.xx_forum.dto;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/12 20:50
 */
public class UploadMsgDTO {
    public int success;
    public String message;
    public String url;

    public UploadMsgDTO() {
    }

    public UploadMsgDTO(int success, String message, String url) {
        this.success = success;
        this.message = message;
        this.url = url;
    }
}
