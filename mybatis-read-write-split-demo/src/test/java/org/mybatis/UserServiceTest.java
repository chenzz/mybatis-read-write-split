package org.mybatis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.bean.User;
import org.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-dao.xml", "classpath:applicationContext-test.xml"})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testGetUserById() {
        User user = userService.getUserById(1);
        Assert.assertNotNull(user);
        System.out.println(user);
    }
}