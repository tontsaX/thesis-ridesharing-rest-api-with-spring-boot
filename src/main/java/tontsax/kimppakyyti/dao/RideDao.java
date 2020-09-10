package tontsax.kimppakyyti.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tontsax.kimppakyyti.logic.Account;
import tontsax.kimppakyyti.logic.Ride;

public interface RideDao extends JpaRepository<Ride, Long> {
	List<Ride> findByOrigin(String origin);
	List<Ride> findByDestination(String destination);
	
}
