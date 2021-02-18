package tontsax.kimppakyyti.services;

import org.springframework.boot.configurationprocessor.json.JSONException;

import tontsax.kimppakyyti.dao.Account;

public interface AccountService {
	public abstract Account registerToApp(String accountJson) throws JSONException;
}
