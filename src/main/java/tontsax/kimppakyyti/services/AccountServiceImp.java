package tontsax.kimppakyyti.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.AccountDao;

@Service
public class AccountServiceImp implements AccountService {
	
	@Autowired
	private AccountDao accountRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Account registerToApp(String accountJson) throws JSONException {
		JSONObject receivedJson = new JSONObject(accountJson);
		
		Account newAccount = new Account();
		newAccount.setNickName(receivedJson.getString("nickName"));
		newAccount.setPassword(passwordEncoder.encode(receivedJson.getString("password")));
		
		return accountRepository.save(newAccount);
	}

	@Override
	public Account getAccountByNickname(String nickName) {
		return accountRepository.findByNickName(nickName);
	}

	@Override
	public Account save(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public Account getAccount(Long id) {
		return accountRepository.getOne(id);
	}

}
