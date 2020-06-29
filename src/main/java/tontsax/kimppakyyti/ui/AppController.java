package tontsax.kimppakyyti.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tontsax.kimppakyyti.dao.RideDao;
import tontsax.kimppakyyti.logic.Ride;

@RestController
//@RequestMapping("/api") // lisää kaikkiin Mappingeihin alkuun osan /api
public class AppController {

	@Autowired
	private RideDao rideDao;
	
	@GetMapping("/rides")
	public List<Ride> getRides() {
		return rideDao.findAll();
	}
	
	@GetMapping("/rides/from{origin}")
	public List<Ride> getRidesByOrigin(@PathVariable String origin) {
		return rideDao.findByOrigin(origin);
	}
	
	@GetMapping("/rides/to{destination}")
	public List<Ride> getRidesByDestination(@PathVariable String destination) {
		return rideDao.findByDestination(destination);
	}
	
	@GetMapping("/rides/{id}")
	public Ride getRide(@PathVariable Long id) {
		return rideDao.getOne(id);
	}
	
	@PostMapping("/rides")
	public Ride postRide(@RequestBody Ride ride) {
		return rideDao.save(ride);
	}
	
	@DeleteMapping("/rides/{id}")
	public Boolean deleteRide(@PathVariable Long id) {
		rideDao.deleteById(id); 
		return !rideDao.existsById(id);
	}
	
}
