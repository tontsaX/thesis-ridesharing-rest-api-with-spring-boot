package tontsax.kimppakyyti.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.AccountDao;
import tontsax.kimppakyyti.dao.Conversation;
import tontsax.kimppakyyti.dao.Message;

@Service
public class AccountServiceImp implements AccountService {
	
	@Autowired
	private AccountDao accountRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ConversationService conversationService;

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

	@Override
	public Message sendMessage(Long conversationId, Message message) {
		if(message == null) {
			return Message.EMPTY;
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
		
		Account sender = accountRepository.findByNickName(auth.getName());
		Account receiver = accountRepository.findByNickName(message.getReceiver());
		
		List<Account> conversationOwners = new ArrayList<>();
		conversationOwners.add(sender);
		conversationOwners.add(receiver);
		
		Conversation conversation = conversationService.getConversation(conversationId);
			
		if(conversation == null) {
			conversation = new Conversation();
			conversation.setOwner(sender);
			conversation.setOwners(conversationOwners);
			
			conversationService.save(conversation);
		} else {
			conversation = conversationService.getConversation(conversationId, conversationOwners);
		}
		
		message.setConversation(conversation);
		
		return conversationService.save(message);
	}

	@Override
	public Conversation getConversation(Long conversationId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Account user = accountRepository.findByNickName(auth.getName());
		List<Account> conversationOwners = new ArrayList<>();
		conversationOwners.add(user);
		
		Conversation conversation = conversationService.getConversation(conversationId, conversationOwners);
		
		if(conversation != null) {
			return conversation;
		}
		
		return Conversation.EMPTY;
	}

}
