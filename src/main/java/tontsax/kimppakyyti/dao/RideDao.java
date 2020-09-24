package tontsax.kimppakyyti.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideDao extends JpaRepository<Ride, Long> {
	
	@EntityGraph(attributePaths = {"driver", "passengers"})
	Ride getOne(Long id);
	
	List<Ride> findByOrigin(String origin);
	List<Ride> findByDestination(String destination);
	
	List<Ride> findByDepartureContaining(String departureDate);
	List<Ride> findByArrivalContaining(String arrivalDate);
}
