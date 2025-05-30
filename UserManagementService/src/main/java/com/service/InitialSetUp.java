package com.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.service.entity.RoleEntity;
import com.service.entity.UserEntity;
import com.service.repository.RoleRepository;
import com.service.repository.UserRepository;
import com.service.security.AppProperties;
import com.service.shared.Role;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InitialSetUp {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private AppProperties properties;

	@Transactional
	@EventListener
	public void setUpAdmin(ApplicationReadyEvent event) {
		log.info("InitialSetUp.setUpAdmin()" + event.getClass().getName());

		saveRoles(Role.ROLE_USER.name());
		RoleEntity roleAdmin = saveRoles(Role.ROLE_ADMIN.name());

		if (roleAdmin == null) {
			log.debug("Admin Role is Null,cannot proceed");
			return;
		}
		UserEntity admin = new UserEntity();
		admin.setId(UUID.randomUUID().toString());
		admin.setName(properties.getAdminName());
		admin.setPassword(encoder.encode(properties.getDefaultAdminPswrd()));
		admin.setEmail(properties.getAdminMail());
		admin.setMobile(Long.parseLong(properties.getAdminMobile()));
		admin.setAddress(properties.getAdminAddress());
		admin.setRoles(new ArrayList<>(Arrays.asList(roleAdmin)));

		if (!userRepository.existsByEmail(admin.getEmail())) {
			userRepository.save(admin);
		}
	}

	private RoleEntity saveRoles(String name) {
		RoleEntity roleEntity = roleRepository.findByName(name);
		if (roleEntity == null) {
			roleEntity = new RoleEntity(name);
			return roleRepository.save(roleEntity);
		}
		return roleEntity;
	}
}