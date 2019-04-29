package com.app.service;

import org.lcn.core.anno.LCNTransactional;
import org.lcn.core.core.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Product;
import com.app.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private HttpClient httpClient;
	@Override
	@Transactional
	@LCNTransactional
	public void insert(Product record) {
		httpClient.get("http://127.0.0.1:8093/add?name=哟个人");
		productMapper.insert(record);
	}
}
