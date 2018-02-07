package org.mybatis;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.bean.User;
import org.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-dao.xml")
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setEmailId("test_email_" + System.currentTimeMillis() + "@gmail.com");
        user.setPassword("secret");
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");

        userMapper.insertUser(user);
        Assert.assertTrue(user.getUserId() != 0);
    }

    @Test
    public void testGetUserById() {
        User user = userMapper.getUserById(1);
        Assert.assertNotNull(user);
        System.out.println(user);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userMapper.getAllUsers();
        Assert.assertNotNull(users);
        for (User user : users) {
            System.out.println(user);
        }

    }


    @Test
    public void testUpdateUser() {
        long timestamp = System.currentTimeMillis();
        User user = userMapper.getUserById(2);
        user.setFirstName("TestFirstName" + timestamp);
        user.setLastName("TestLastName" + timestamp);
    }

    @Test
    public void testDeleteUser() {
        User deletedUser = userMapper.getUserById(4);
        Assert.assertNull(deletedUser);

    }
}