package com.increff.pos.service;

import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import com.increff.pos.util.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class UserService {

	@Autowired
	private UserDao dao;

	 
	public void add(UserPojo userPojo) throws ApiException {
		UserPojo existingUser = dao.selectByColumn("email",userPojo.getEmail());
		if (Objects.nonNull(existingUser)) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(userPojo);
	}

	  
	public UserPojo get(String email) throws ApiException {
		return dao.selectByColumn("email", email);
	}

	 
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	 
	public void delete(Integer userId) {
		dao.deleteById(userId);
	}

}
