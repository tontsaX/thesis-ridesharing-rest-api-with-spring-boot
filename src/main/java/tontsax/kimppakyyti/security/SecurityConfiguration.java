package tontsax.kimppakyyti.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().sameOrigin();
		
		httpSecurity.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/rides/**").permitAll()
					.antMatchers("/register").permitAll()
					.anyRequest().authenticated().and()
					.formLogin().permitAll().and()
					.logout().permitAll();
		httpSecurity.csrf().disable();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authManBuilder) throws Exception {
		authManBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
