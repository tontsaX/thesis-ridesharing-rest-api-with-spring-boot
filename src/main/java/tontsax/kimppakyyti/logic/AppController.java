package tontsax.kimppakyyti.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tontsax.kimppakyyti.dao.AccountDao;
import tontsax.kimppakyyti.dao.RideDao;

@RestController
//@RequestMapping("/api") // lisää kaikkiin Mappingeihin alkuun osan /api
public class AppController {

	@Autowired
	private RideDao rideDao;
	
	@Autowired
	private AccountDao accountDao;
	
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
//	@PostMapping(path = "/rides", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
//	@PostMapping(path = "/rides", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Ride postRide(@RequestBody Ride ride) {
		Ride newRide = new Ride();
		newRide.setDestination(ride.getDestination());
		newRide.setOrigin(ride.getOrigin());
		newRide.setPrice(ride.getPrice());
		return rideDao.save(newRide);
	}
	
	@DeleteMapping("/rides/{id}")
	public Boolean deleteRide(@PathVariable Long id) {
		rideDao.deleteById(id); 
		return !rideDao.existsById(id);
	}
	
	@PutMapping("/rides/{id}")
	public Ride updateRide(@RequestBody Ride ride, @PathVariable Long id) {
		Ride updatedRide = rideDao.findById(id).get();
		updatedRide.setOrigin(ride.getOrigin());
		updatedRide.setDestination(ride.getDestination());
		updatedRide.setPrice(ride.getPrice());
		return rideDao.save(updatedRide);
	}
	
}
