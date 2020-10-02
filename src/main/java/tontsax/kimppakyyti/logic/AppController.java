package tontsax.kimppakyyti.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.AccountDao;
import tontsax.kimppakyyti.dao.Ride;
import tontsax.kimppakyyti.dao.RideDao;

@RestController
//@RequestMapping("/api") // lisää kaikkiin Mappingeihin alkuun osan /api
public class AppController {

	@Autowired
	private RideDao rideDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@GetMapping("/rides")
	public Page<Ride> getRides() {
		Pageable sorter = PageRequest.of(0,10,Sort.by("departure").ascending());
		return rideDao.findAll(sorter);
	}
	
	@GetMapping("/rides/from{origin}")
	public List<Ride> getRidesByOrigin(@PathVariable String origin) {
		return rideDao.findByOrigin(origin);
	}
	
	@GetMapping("/rides/to{destination}")
	public List<Ride> getRidesByDestination(@PathVariable String destination) {
		return rideDao.findByDestination(destination);
	}
	
	@GetMapping("/rides/departure")
	public List<Ride> getRidesByDeparture(@RequestParam String departure) {
		return rideDao.findByDepartureContaining(trimToDate(departure));
	}
	
	@GetMapping("/rides/arrival")
	public List<Ride> getRidesByArrival(@RequestParam String arrival) {
		return rideDao.findByArrivalContaining(trimToDate(arrival));
	}
	
	private String trimToDate(String localDateTime) {
		LocalDateTime dateTime = LocalDateTime.parse(localDateTime);
		LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
		String trimmedDate = date.toString();
		return trimmedDate;
	}
	
	@GetMapping("/rides/{id}")
	public Ride getRide(@PathVariable Long id) {
		return rideDao.getOne(id);
	}
	
	@PostMapping("/rides")
	public Ride postRide(@RequestBody String rideJson) throws JSONException {
		JSONObject receivedJson = new JSONObject(rideJson);
		
		Ride newRide = new Ride();
		newRide.setOrigin(receivedJson.getString("origin"));
		newRide.setDestination(receivedJson.getString("destination"));
		newRide.setPrice(receivedJson.getDouble("price"));
		newRide.setDriver(accountDao.getOne(receivedJson.getLong("driverId")));
		newRide.setDeparture(receivedJson.getString("departure"));
		newRide.setArrival(receivedJson.getString("arrival"));
		
		return rideDao.save(newRide);
	}
	
	@DeleteMapping("/rides/{id}")
	public Boolean deleteRide(@PathVariable Long id) {
		rideDao.deleteById(id); 
		return !rideDao.existsById(id);
	}
	
	@PutMapping("/rides/{id}")
	public Ride updateRide(@RequestBody String rideJson, @PathVariable Long id) throws JSONException {
		JSONObject receivedJson = new JSONObject(rideJson);
		
		Ride updatedRide = rideDao.getOne(id);
		
		updatedRide.setOrigin(receivedJson.getString("origin"));
		updatedRide.setDestination(receivedJson.getString("destination"));
		updatedRide.setPrice(receivedJson.getDouble("price"));
		updatedRide.setDeparture(receivedJson.getString("departure"));
		updatedRide.setArrival(receivedJson.getString("arrival"));

		return rideDao.save(updatedRide);
	}
	
	@PostMapping("/register")
	public Account registerToApp(@RequestBody String accountJson) throws JSONException {
		JSONObject receivedJson = new JSONObject(accountJson);
		
		Account newAccount = new Account();
		newAccount.setNickName(receivedJson.getString("nickName"));
		
		return accountDao.save(newAccount);
	}
	
}
