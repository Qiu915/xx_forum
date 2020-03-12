package com.xp.xx_forum.dto;

import com.xp.xx_forum.exception.CustomizeErrorCode;
import com.xp.xx_forum.exception.CustomizeException;
import lombok.Data;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/8 15:53
 */
@Data
public class ResultDTO<T> {

    private Integer code;
    private String message;
    private T data;

    public static ResultDTO error(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static ResultDTO error(CustomizeErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResultDTO error(CustomizeException e) {
        return error(e.getCode(), e.getMessage());
    }

    public static ResultDTO success() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO success(T data) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(data);
        return resultDTO;
    }

}
