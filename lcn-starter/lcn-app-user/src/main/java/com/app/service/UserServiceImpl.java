package com.app.service;

import org.lcn.core.anno.LCNTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.User;
import com.app.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper productMapper;

	@Override
	@Transactional
	@LCNTransactional(isEnd=true)
	public void insert(User record) {
		productMapper.insert(record);
	}
}
