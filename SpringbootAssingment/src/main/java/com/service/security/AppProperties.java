package com.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

	@Autowired
	private Environment environment;

	public String getSecret() {
		return environment.getProperty("token.secret");
	}

	public String getExpirationTime() {
		return environment.getProperty("token.expirationTime");
	}

	public String getDefaultAdminPswrd() {
		return environment.getProperty("admin.password");
	}

	public String getAdminMail() {
		return environment.getProperty("admin.mail");
	}

	public String getAdminName() {
		return environment.getProperty("admin.name");
	}

	public String getAdminAddress() {
		return environment.getProperty("admin.address");
	}

	public String getAdminMobile() {
		return environment.getProperty("admin.mobile");
	}
}