package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Order;
import com.app.service.OrderService;

@RestController
public class OrderController {
	@Autowired
	private OrderService orderService;

	@RequestMapping("/add")
	public int insert(Order record) {
		orderService.insert(record);
		return 1;
	}

}
