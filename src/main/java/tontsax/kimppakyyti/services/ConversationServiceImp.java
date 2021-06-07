package tontsax.kimppakyyti.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.Conversation;
import tontsax.kimppakyyti.dao.ConversationDao;
import tontsax.kimppakyyti.dao.Message;
import tontsax.kimppakyyti.dao.MessageDao;

@Service
public class ConversationServiceImp implements ConversationService {
	
	@Autowired
	private ConversationDao conversationRepository;
	
	@Autowired
	private MessageDao messageRepository;

	@Override
	public Conversation getConversation(Long id) {
		try {
			return conversationRepository.findById(id).get();
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public Conversation getConversation(Account owner, Account receiver) {
		return null;
	}

	@Override
	public Conversation save(Conversation conversation) {
		return conversationRepository.save(conversation);
	}

	@Override
	public Message save(Message message) {
		return messageRepository.save(message);
	}

	@Override
	public Message save(Conversation conversation, Message message) {
		message.setConversation(conversation);
		message = messageRepository.save(message);
		
		return message;
	}

	@Override
	public List<Conversation> getConversations(Account owner) {
		return conversationRepository.findByOwner(owner);
	}

	@Override
	public Conversation getConversation(Long id, Account owner) {
		return conversationRepository.findByIdAndOwner(id, owner);
	}

	@Override
	public Conversation getConversation(Long id, List<Account> owners) {
		return conversationRepository.findByIdAndOwnersIn(id, owners);
	}

}
