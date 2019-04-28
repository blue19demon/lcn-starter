package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Product;
import com.app.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;

	@RequestMapping("/add")
	public int insert(Product record) {
		productService.insert(record);
		return 1;
	}

}
