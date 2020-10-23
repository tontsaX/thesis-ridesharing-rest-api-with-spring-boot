package tontsax.kimppakyyti.dao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDao extends JpaRepository<Account, Long> {
	Account findByNickName(String nickName);
}
