package tontsax.kimppakyyti.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
					.antMatchers(HttpMethod.GET, "/rides/**").permitAll()
					.anyRequest().authenticated().and()
					.formLogin().permitAll().and()
					.logout().permitAll();
	}
}
