package com.xp.xx_forum.utils;

import com.xp.xx_forum.enums.LikedType;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/15 13:40
 */
public class RedisUtil {
    public static String getId(String id,Integer type){
        if(type == LikedType.COMMENT.getType()){
            String newId = "comment:"+id;
            return newId;
        }else{
            String newId = "question:"+id;
            return newId;
        }
    }

}
