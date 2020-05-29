package tontsax.kimppakyyti.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import tontsax.kimppakyyti.logic.Ride;

public interface RideDao extends JpaRepository<Ride, Long> {

}
