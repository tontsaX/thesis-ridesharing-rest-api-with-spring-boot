package tontsax.kimppakyyti.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RideDao extends JpaRepository<Ride, Long> {
	List<Ride> findByOrigin(String origin);
	List<Ride> findByDestination(String destination);
	
	// näitä tarvitsee tarkentaa jotenkin
	// LocalDateTime-oliolla ei ole saanut tuloksia
	// muutenkin tarvitsisi olla haku, jossa haetaan pvm perusteella eikä pvm ja kellonajalla
	List<Ride> findByDeparture(LocalDateTime departure);
	List<Ride> findByArrival(LocalDateTime arrival);
	List<Ride> findByDepartureLike(LocalDateTime departure);
//	List<Ride> findByDepartureLike(String departure);
}
