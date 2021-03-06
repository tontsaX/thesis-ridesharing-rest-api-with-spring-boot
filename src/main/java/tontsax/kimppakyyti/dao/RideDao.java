package tontsax.kimppakyyti.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideDao extends JpaRepository<Ride, Long> {
	
	@EntityGraph(attributePaths = {"driver", "passengers"})
	Ride getOne(Long id);
	
	List<Ride> findByOrigin(String origin, Pageable page);
	List<Ride> findByDestination(String destination, Pageable page);
	
	List<Ride> findByDepartureContaining(String departureDate, Pageable page);
	List<Ride> findByArrivalContaining(String arrivalDate, Pageable page);
}
