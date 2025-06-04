package com.service.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.service.dto.UserRequest;
import com.service.dto.UserResponse;
import com.service.exception.UserNotFoundException;

public interface UserService extends UserDetailsService {
	boolean addUser(UserRequest request);

	List<UserResponse> getAllUsers();

	UserResponse findById(String id) throws UserNotFoundException;

	String deleteUserById(String userId) throws UserNotFoundException;
}