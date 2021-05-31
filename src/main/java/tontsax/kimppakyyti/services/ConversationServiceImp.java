package tontsax.kimppakyyti.services;

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
		return conversationRepository.findById(id).get();
	}

	@Override
	public Conversation getConversation(Account owner, Account receiver) {
//		return conversationRepository.findByOwnerAndReceiver(owner, receiver);
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
//		conversation = conversationRepository.save(conversation);
		
		message.setConversation(conversation);
		message = messageRepository.save(message);
		
		System.out.println("CONV ID: " + conversation.getId());
		System.out.println("MESSAGE ID: " + message.getId());
		System.out.println("MESSAGE: " + message);
		
//		conversation.getMessages().add(messageRepository.save(message));
		
//		conversationRepository.save(conversation);
		
		return message;
	}

}
