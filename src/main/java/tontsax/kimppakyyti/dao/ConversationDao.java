package tontsax.kimppakyyti.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationDao extends JpaRepository<Conversation, Long> {
	Conversation findByIdAndOwnersIn(Long id, List<Account> owners);
	List<Conversation> findByOwnersIn(List<Account> owners);
}
