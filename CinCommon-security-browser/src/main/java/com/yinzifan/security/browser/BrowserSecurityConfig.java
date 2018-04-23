package com.yinzifan.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.yinzifan.core.properties.SecurityProperties;
import com.yinzifan.security.browser.authentication.CustAuthenticationFailureHandler;
import com.yinzifan.security.browser.authentication.CustAuthenticationSuccessHandler;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private CustAuthenticationSuccessHandler custAuthenticationSuccessHandler; 
	
	@Autowired
	private CustAuthenticationFailureHandler custAuthenticationFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin() // 用表单登录进行身份认证
			.successHandler(custAuthenticationSuccessHandler)
			.failureHandler(custAuthenticationFailureHandler)
			.loginPage("/authentication/require")
			.loginProcessingUrl("/authentication/form")
			.and() // 
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/authentication/require", 
						securityProperties.getBrowser().getLoginPage())
						.permitAll()
			.anyRequest() // 任何请求
			.authenticated(); // 都要进行身份认证
			
	}
}
