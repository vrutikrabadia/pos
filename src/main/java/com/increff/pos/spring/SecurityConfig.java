package com.increff.pos.spring;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger logger = Logger.getLogger(SecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http//
			// Match only these URLs
				.requestMatchers()//
				.antMatchers("/api/**")//
				.antMatchers("/ui/**")//
				.and().authorizeRequests()//
				.antMatchers("/api/about/**").permitAll()//
				.antMatchers("/api/orders/**").permitAll()
				.antMatchers("/api/inventory/**").permitAll()
				.antMatchers("/api/products/**").permitAll()
				.antMatchers("/api/brands/**").permitAll()// For testing permits access to endpoInteger without authemtication
				.antMatchers("/api/admin/**").hasAuthority("admin")//
				.antMatchers("/api/**").hasAnyAuthority("admin", "standard")//
				.antMatchers("/ui/admin/**").hasAuthority("admin")//
				.antMatchers("/ui/**").hasAnyAuthority("admin", "standard")//
				// Ignore CSRF and CORS
				.and().csrf().disable().cors().disable();
		logger.info("Configuration complete");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**");
	}

}
