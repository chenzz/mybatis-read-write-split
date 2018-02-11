package org.mybatis.service;

import org.mybatis.bean.User;
import org.mybatis.mapper.UserMapper;
import org.mybatis.rw.anno.DataSource;
import org.mybatis.rw.constant.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    @DataSource(DataSourceType.MASTER)
    public User getUserById(Integer userId) {

        return userMapper.getUserById(userId);
    }

}