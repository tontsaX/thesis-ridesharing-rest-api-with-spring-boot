package tontsax.kimppakyyti.services;

import org.springframework.boot.configurationprocessor.json.JSONException;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.Conversation;
import tontsax.kimppakyyti.dao.Message;

public interface AccountService {
	public abstract Account getAccount(Long id);
	public abstract Account getAccountByNickname(String nickName);
	public abstract Account registerToApp(String accountJson) throws JSONException;
	
	public abstract Account save(Account account);
	
	public abstract Message sendMessage(Long id, Message message);
	public abstract Conversation getConversation(Long id);
}
