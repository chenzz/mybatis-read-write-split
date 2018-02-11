package org.mybatis.mapper;

import org.mybatis.bean.User;

import java.util.List;

public interface UserMapper {

    void insertUser(User user);

    User getUserById(Integer userId);

    User getUserById2(Integer userId);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(Integer userId);

}