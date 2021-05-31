package tontsax.kimppakyyti.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationDao extends JpaRepository<Conversation, Long> {
//	Conversation findByOwnerAndReceiver(Account owner, Account receiver);
}
