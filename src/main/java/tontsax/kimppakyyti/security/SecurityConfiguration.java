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
					.antMatchers(HttpMethod.GET, "/rides").permitAll()
//					.antMatchers(HttpMethod.POST, "/rides").authenticated();
					.anyRequest().authenticated();
					
//        httpSecurity.authorizeRequests().antMatchers("/**").permitAll();
//		httpSecurity.csrf().disable();
//		httpSecurity.headers().frameOptions().disable();
	}
}
