package com.service.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
	private String id;
	private String password;
}