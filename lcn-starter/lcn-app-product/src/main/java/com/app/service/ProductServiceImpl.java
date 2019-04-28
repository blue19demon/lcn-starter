package com.app.service;

import org.lcn.core.anno.LubanTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Product;
import com.app.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;

	@Override
	@Transactional
	@LubanTransactional(isEnd=true)
	public void insert(Product record) {
		productMapper.insert(record);
	}
}
