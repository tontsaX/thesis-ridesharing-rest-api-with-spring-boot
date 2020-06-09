package tontsax.kimppakyyti.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tontsax.kimppakyyti.dao.RideDao;
import tontsax.kimppakyyti.logic.Ride;

@RestController
public class AppController {

	@Autowired
	private RideDao rideDao;
	
	@GetMapping("/rides")
	public List<Ride> getRides() {
		return rideDao.findAll();
	}
	
	@GetMapping("/rides/{id}")
	public Ride getRide(@PathVariable Long id) {
		return rideDao.getOne(id);
	}
	
	@PostMapping("/rides")
	public Ride postRide(@RequestBody Ride ride) {
		return null;
	}
	
	@DeleteMapping("/rides/{id}")
	public Ride deleteRide(@PathVariable Long id) {
		return null;
	}
	
}
