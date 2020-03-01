package com.xp.xx_forum.service;

import com.xp.xx_forum.bean.User;
import com.xp.xx_forum.bean.UserExample;
import com.xp.xx_forum.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author ph
 * @version 1.0
 * @date 2020/2/29 17:28
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdateUser(User user) {

        UserExample example = new UserExample();
        example.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(example);
        if(users.size()==0){
//            数据库中没有用户信息则为第一次登录，将信息插入数据库
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(System.currentTimeMillis());
            userMapper.insert(user);
        }else{
//数据库中如存在用户，则更新该用户信息
            user.setGmtModified(System.currentTimeMillis());
            UserExample example1 = new UserExample();
            example1.createCriteria()
                    .andIdEqualTo(users.get(0).getId());
            userMapper.updateByExampleSelective(user, example1);
        }
    }

}
