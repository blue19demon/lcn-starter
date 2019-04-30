package com.app.service;

import org.lcn.core.anno.LCNTransactional;
import org.lcn.core.core.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.Order;
import com.app.mapper.OrderMapper;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private HttpClient httpClient;

	@Override
	@Transactional
	@LCNTransactional(isStart=true)
	public void insert(Order record) {
		httpClient.get("http://127.0.0.1:8092/add?name=手机");
		orderMapper.insert(record);
		//System.out.println(1/0);
	}
}
