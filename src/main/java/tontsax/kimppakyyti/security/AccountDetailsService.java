package tontsax.kimppakyyti.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.AccountDao;

@Service
public class AccountDetailsService implements UserDetailsService {

	@Autowired
	private AccountDao accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByNickName(username);
		
		if(account == null) {
			throw new UsernameNotFoundException("No such user: " + username);
		}
		
		return new org.springframework.security.core.userdetails.User(
				account.getNickName(),
				account.getPassword(),
				true,
				true,
				true,
				true,
				null);
//				Arrays.asList(new SimpleGrantedAuthority("USER")));
	}

}
