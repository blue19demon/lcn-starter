package com.app.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.app.AppProductApplication;
import com.app.entity.Product;
import com.app.service.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { AppProductApplication.class })
public class AppTests {

	@Autowired
	private ProductService productService;

	@Test
	public void test() {
		Product order = Product.builder().name("商品").build();
		productService.insert(order);
	}

}
