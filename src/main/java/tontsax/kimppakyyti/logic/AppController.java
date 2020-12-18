package tontsax.kimppakyyti.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@GetMapping("/rides")
	public Page<Ride> getRides(@RequestParam(defaultValue = "0") Integer page) {
		return rideDao.findAll(getNextPageOrderByDeparture(page));
	}
	
	@GetMapping("/rides/from{origin}")
	public List<Ride> getRidesByOrigin(@PathVariable String origin, @RequestParam(defaultValue = "0") Integer page) {
		return rideDao.findByOrigin(origin, getNextPageOrderByDeparture(page));
	}
	
	@GetMapping("/rides/to{destination}")
	public List<Ride> getRidesByDestination(@PathVariable String destination, @RequestParam(defaultValue = "0") Integer page) {
		return rideDao.findByDestination(destination, getNextPageOrderByDeparture(page));
	}
	
	@GetMapping("/rides/departure")
	public List<Ride> getRidesByDeparture(@RequestParam String departure, @RequestParam(defaultValue = "0") Integer page) {
		return rideDao.findByDepartureContaining(departure, getNextPageOrderByDeparture(page));
	}
	
	@GetMapping("/rides/arrival")
	public List<Ride> getRidesByArrival(@RequestParam String arrival, @RequestParam(defaultValue = "0") Integer page) {
		return rideDao.findByArrivalContaining(arrival, getNextPageOrderByDeparture(page));
	}
	
	private Pageable getNextPageOrderByDeparture(Integer page) {
		return PageRequest.of(page,10,Sort.by("departure").ascending());
	}
	
	@GetMapping("/rides/{id}")
	public Ride getRide(@PathVariable Long id) {
		return rideDao.getOne(id);
	}
	
	@PostMapping("/rides")
	public Ride postRide(@RequestBody String rideJson) throws JSONException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			Ride newRide = new Ride();
			JSONObject receivedJson = new JSONObject(rideJson);
			
			newRide.setOrigin(receivedJson.getString("origin"));
			newRide.setDestination(receivedJson.getString("destination"));
			newRide.setPrice(receivedJson.getDouble("price"));
			newRide.setDriver(accountDao.findByNickName(auth.getName()));
			newRide.setDeparture(receivedJson.getString("departure"));
			newRide.setArrival(receivedJson.getString("arrival"));
			
			return rideDao.save(newRide);
		}

		return Ride.EMPTY;
	}
	
	@PutMapping("/rides/{id}")
	public List<Ride> addPassenger(@PathVariable Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			Account passenger = accountDao.findByNickName(auth.getName());
			Ride ride = rideDao.getOne(id);
			
			passenger.getReservedRides().add(ride);
			accountDao.save(passenger);
			
			return accountDao.getOne(passenger.getId()).getReservedRides();
		}
		
		return Collections.emptyList();
	}
	
	@DeleteMapping("/account/rides/{id}")
	public Boolean deleteRide(@PathVariable Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			Long accountId = accountDao.findByNickName(auth.getName()).getId();
			Ride deletedRide = rideDao.getOne(id);
			
			if(deletedRide.getDriver().getId() == accountId) {
				rideDao.deleteById(id);
			}
		}
		
		return !rideDao.existsById(id);
	}
	
	@PutMapping("/account/rides/{id}")
	public Ride updateRide(@RequestBody String rideJson, @PathVariable Long id) throws JSONException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			Long accountId = accountDao.findByNickName(auth.getName()).getId();
			Ride updatedRide = rideDao.getOne(id);
			
			if(updatedRide.getDriver().getId() == accountId) {
				JSONObject receivedJson = new JSONObject(rideJson);
				
				updatedRide.setOrigin(receivedJson.getString("origin"));
				updatedRide.setDestination(receivedJson.getString("destination"));
				updatedRide.setPrice(receivedJson.getDouble("price"));
				updatedRide.setDeparture(receivedJson.getString("departure"));
				updatedRide.setArrival(receivedJson.getString("arrival"));
				
				return rideDao.save(updatedRide);
			}
		}

		return Ride.EMPTY;
	}
	
	@PostMapping("/register")
	public Account registerToApp(@RequestBody String accountJson) throws JSONException {
		JSONObject receivedJson = new JSONObject(accountJson);
		
		Account newAccount = new Account();
		newAccount.setNickName(receivedJson.getString("nickName"));
		newAccount.setPassword(passwordEncoder.encode(receivedJson.getString("password")));
		
		return accountDao.save(newAccount);
	}
	
}
