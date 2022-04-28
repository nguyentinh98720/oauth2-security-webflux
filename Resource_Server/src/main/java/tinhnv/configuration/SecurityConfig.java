package tinhnv.configuration;

import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// to use https protocol on heroku app
		http.requiresChannel()
	      .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
	      .requiresSecure();
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET).hasAuthority("SCOPE_read")
		.antMatchers(HttpMethod.POST).hasAuthority("SCOPE_manage")
		.antMatchers(HttpMethod.PUT).hasAuthority("SCOPE_manage")
		.antMatchers(HttpMethod.DELETE).hasAuthority("SCOPE_manage")
		.and()
		.oauth2ResourceServer().jwt();
	}
}
