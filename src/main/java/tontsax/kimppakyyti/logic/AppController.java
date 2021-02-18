package tontsax.kimppakyyti.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.Ride;
import tontsax.kimppakyyti.services.AccountService;
import tontsax.kimppakyyti.services.RideService;

@RestController
//@RequestMapping("/api")
public class AppController {;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RideService rideService;
	
	
	@GetMapping("/rides")
	public List<Ride> getRides(@RequestParam(defaultValue = "0") Integer page) {
		return rideService.getRides(page);
	}
	
	@GetMapping("/rides/from{origin}")
	public List<Ride> getRidesByOrigin(@PathVariable String origin, @RequestParam(defaultValue = "0") Integer page) {
		return rideService.getRidesByOrigin(origin, page);
	}
	
	@GetMapping("/rides/to{destination}")
	public List<Ride> getRidesByDestination(@PathVariable String destination, @RequestParam(defaultValue = "0") Integer page) {
		return rideService.getRidesByDestination(destination, page);
	}
	
	@GetMapping("/rides/departure")
	public List<Ride> getRidesByDeparture(@RequestParam String departure, @RequestParam(defaultValue = "0") Integer page) {
		return rideService.getRidesByDeparture(departure, page);
	}
	
	@GetMapping("/rides/arrival")
	public List<Ride> getRidesByArrival(@RequestParam String arrival, @RequestParam(defaultValue = "0") Integer page) {
		return rideService.getRidesByArrival(arrival, page);
	}
	
	@GetMapping("/rides/{id}")
	public Ride getRide(@PathVariable Long id) {
		return rideService.getRide(id);
	}
	
	@PostMapping("/rides")
	public Ride postRide(@RequestBody String rideJson) throws JSONException {
		return rideService.createRide(rideJson);
	}
	
	@PutMapping("/rides/{id}")
	public List<Ride> addPassenger(@PathVariable Long id) {
		return rideService.addPassenger(id);
	}
	
	@PutMapping("/rides/{id}/cancel")
	public List<Ride> cancelRide(@PathVariable Long id) {
		return rideService.cancelRide(id);
	}
	
	@DeleteMapping("/account/rides/{id}")
	public Boolean deleteRide(@PathVariable Long id) {
		return rideService.deleteRide(id);
	}
	
	@PutMapping("/account/rides/{id}")
	public Ride updateRide(@RequestBody String rideJson, @PathVariable Long id) throws JSONException {
		return rideService.updateRide(rideJson, id);
	}
	
	@PostMapping("/register")
	public Account registerToApp(@RequestBody String accountJson) throws JSONException {
		return accountService.registerToApp(accountJson);
	}
	
}
