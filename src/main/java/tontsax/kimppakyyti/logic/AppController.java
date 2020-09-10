package tontsax.kimppakyyti.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
//		databasePopulationLiveTest();
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
	public Ride postRide(@RequestBody String rideJson) throws JSONException {
		JSONObject receivedJson = new JSONObject(rideJson);
		
		Ride newRide = new Ride();
		newRide.setOrigin(receivedJson.getString("origin"));
		newRide.setDestination(receivedJson.getString("destination"));
		newRide.setPrice(receivedJson.getDouble("price"));
		newRide.setDriver(accountDao.getOne(receivedJson.getLong("driverId")));
//		newRide.setDriver(accountDao.findById(receivedJson.getLong("driverId")).get());
		// kyytiä postatessa ei tule matkustajalistaa vielä
		// matkustaja listaa päivitetään "yksi kerrallaan" sitä mukaan, kun joku käyttäjä varaa itsellensä kyydin
		
		return rideDao.save(newRide);
	}
	
	@DeleteMapping("/rides/{id}")
	public Boolean deleteRide(@PathVariable Long id) {
		rideDao.deleteById(id); 
		return !rideDao.existsById(id);
	}
	
	@PutMapping("/rides/{id}")
	public Ride updateRide(@RequestBody String rideJson, @PathVariable Long id) throws JSONException {
//		Ride updatedRide = rideDao.findById(id).get();
		JSONObject receivedJson = new JSONObject(rideJson);
		
		Ride updatedRide = rideDao.getOne(id);
		
		updatedRide.setOrigin(receivedJson.getString("origin"));
		updatedRide.setDestination(receivedJson.getString("destination"));
		updatedRide.setPrice(receivedJson.getDouble("price"));

		return rideDao.save(updatedRide);
	}
	
	private void databasePopulationLiveTest() {
		Account account1 = new Account();
		Account account2 = new Account();
		
		account1.setNickName("Decimus");
		account2.setNickName("Tilemar");
		
		accountDao.save(account1);
		accountDao.save(account2);
		
		Ride ride1 = new Ride("Turku", "Helsinki", 10.0);
		Ride ride2 = new Ride("Turku", "Tampere", 23.5);
		
//		ride1.setDriver(account1);
		ride1.setDriver(accountDao.getOne(1L));
		ride2.setDriver(account2);
		rideDao.save(ride1);
		rideDao.save(ride2);
	}
	
}
