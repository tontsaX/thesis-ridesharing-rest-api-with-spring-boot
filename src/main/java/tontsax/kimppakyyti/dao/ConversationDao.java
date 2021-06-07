package tontsax.kimppakyyti.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationDao extends JpaRepository<Conversation, Long> {
//	Conversation findByOwnerAndReceiver(Account owner, Account receiver);
	List<Conversation> findByOwner(Account owner);
	Conversation findByIdAndOwner(Long id, Account owner);
}
