package com.service.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.constants.SecurityConstants;
import com.service.dto.UserRequest;
import com.service.dto.UserResponse;
import com.service.entity.RoleEntity;
import com.service.entity.UserEntity;
import com.service.exception.NoDataFoundException;
import com.service.exception.UserNotFoundException;
import com.service.repository.RoleRepository;
import com.service.repository.UserRepository;
import com.service.security.UserPrincipal;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Override
	public boolean addUser(UserRequest request) {
		UserEntity entity = new UserEntity();
		BeanUtils.copyProperties(request, entity);

		entity.setId(UUID.randomUUID().toString());
		String passowrd = encoder.encode(request.getPassword());
		entity.setPassword(passowrd);

		Collection<RoleEntity> roleEntities = new ArrayList<RoleEntity>();
		request.getRoles().forEach(role -> {
			RoleEntity roleEntity = roleRepository.findByName(role);
			if (roleEntity != null) {
				roleEntities.add(roleEntity);
			}
		});
		entity.setRoles(roleEntities);
		UserEntity userEntity = userRepository.save(entity);
		return userEntity != null;
	}

	@Override
	public UserResponse findById(String id) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));

		Collection<String> roles = new HashSet<>();
		entity.getRoles().forEach(roleEntity -> roles.add(roleEntity.getName()));
		UserResponse userResponse = new UserResponse();

		BeanUtils.copyProperties(entity, userResponse);
		userResponse.setRoles(roles);
		return userResponse;
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserEntity entity = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException(SecurityConstants.USER_NOT_FOUND));
		return new UserPrincipal(entity);
	}

	@Override
	public List<UserResponse> getAllUsers() {
		List<UserEntity> list = userRepository.findAll();
		if (!list.isEmpty()) {
			List<UserResponse> responses = new ArrayList<>();
			for (UserEntity entity : list) {
				Collection<String> roles = new HashSet<>();
				Collection<RoleEntity> roleEntities = entity.getRoles();
				roleEntities.forEach(roleEntity -> roles.add(roleEntity.getName()));
				UserResponse userResponse = new UserResponse();

				BeanUtils.copyProperties(entity, userResponse);
				userResponse.setRoles(roles);

				responses.add(userResponse);
			}
			return responses;
		}
		throw new NoDataFoundException(SecurityConstants.DATA_NOT_FOUND);
	}

	@Override
	public String deleteUserById(String userId) throws UserNotFoundException {
		UserEntity entity = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(SecurityConstants.USER_NOT_FOUND));
		userRepository.delete(entity);
		return SecurityConstants.USER_DELETED + " " + userId;
	}
}