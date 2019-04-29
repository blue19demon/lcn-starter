package com.app.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.app.AppUserApplication;
import com.app.entity.User;
import com.app.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AppUserApplication.class })
public class AppTests {

	@Autowired
	private UserService userService;

	@Test
	public void test() {
		User user = User.builder().name("这两个").age(32).build();
		userService.insert(user);
	}

}
