package com.service.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.service.dto.UserRequest;
import com.service.dto.UserResponse;
import com.service.exception.UserNotFoundException;
import com.service.service.UserService;
import com.service.shared.Role;

@RestController
public class UserController {

	@Autowired
	private UserService service;


	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserRequest request) throws IOException {
		request.setRoles(new HashSet<>(Arrays.asList(Role.ROLE_USER.name())));
		boolean user = service.addUser(request);

		return user ? ResponseEntity.status(HttpStatus.CREATED).body("Created")
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some Exception");
	}

	@GetMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN') or #id==principal.userId")
	public ResponseEntity<UserResponse> findByUserId(@PathVariable String id) throws UserNotFoundException {
		UserResponse userResponse = service.findById(id);
		return userResponse != null ? ResponseEntity.ok(userResponse) : ResponseEntity.notFound().build();
	}

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> users = service.getAllUsers();
		if (users.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
		}
		return ResponseEntity.status(HttpStatus.FOUND).body(users);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUserData(@PathVariable String id) throws UserNotFoundException {
		String response = service.deleteUserById(id);
		return ResponseEntity.status(HttpStatus.GONE).body(response);
	}
}