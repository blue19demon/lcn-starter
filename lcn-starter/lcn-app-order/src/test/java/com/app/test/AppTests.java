package com.app.test;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.app.AppOrderApplication;
import com.app.entity.Order;
import com.app.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AppOrderApplication.class })
public class AppTests {

	@Autowired
	private OrderService orderService;

	@Test
	public void test() {
		Order order = Order.builder().name("京东订单").remark("手机壳").price(new BigDecimal(199)).build();
		orderService.insert(order);
	}

}
