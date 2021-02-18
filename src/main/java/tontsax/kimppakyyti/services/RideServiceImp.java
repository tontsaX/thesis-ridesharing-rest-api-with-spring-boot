package tontsax.kimppakyyti.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.Ride;
import tontsax.kimppakyyti.dao.RideDao;

@Service
public class RideServiceImp implements RideService {
	
	@Autowired
	private RideDao rideRepository;
	
	@Autowired
	private AccountService accountService;

	@Override
	public List<Ride> getRides(Integer page) {
		return rideRepository.findAll(getNextPageOrderByDeparture(page)).getContent();
	}

	@Override
	public List<Ride> getRidesByOrigin(String origin, Integer page) {
		return rideRepository.findByOrigin(origin, getNextPageOrderByDeparture(page));
	}

	@Override
	public List<Ride> getRidesByDestination(String destination, Integer page) {
		return rideRepository.findByDestination(destination, getNextPageOrderByDeparture(page));
	}

	@Override
	public List<Ride> getRidesByDeparture(String departure, Integer page) {
		return rideRepository.findByDepartureContaining(departure, getNextPageOrderByDeparture(page));
	}

	@Override
	public List<Ride> getRidesByArrival(String arrival, Integer page) {
		return rideRepository.findByArrivalContaining(arrival, getNextPageOrderByDeparture(page));
	}

	@Override
	public Ride getRide(Long id) {
		return rideRepository.getOne(id);
	}

	@Override
	public Ride createRide(String rideJson) throws JSONException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			Ride newRide = new Ride();
			JSONObject receivedJson = new JSONObject(rideJson);
			
			newRide.setOrigin(receivedJson.getString("origin"));
			newRide.setDestination(receivedJson.getString("destination"));
			newRide.setPrice(receivedJson.getDouble("price"));
//			newRide.setDriver(accountRepository.findByNickName(auth.getName()));
			newRide.setDriver(accountService.getAccountByNickname(auth.getName()));
			newRide.setDeparture(receivedJson.getString("departure"));
			newRide.setArrival(receivedJson.getString("arrival"));
			
			return rideRepository.save(newRide);
		}

		return Ride.EMPTY;
	}

	@Override
	public Ride updateRide(String rideJson, Long id) throws JSONException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
//			Long accountId = accountRepository.findByNickName(auth.getName()).getId();
			Long accountId = accountService.getAccountByNickname(auth.getName()).getId();
			Ride updatedRide = rideRepository.getOne(id);
			
			if(updatedRide.getDriver().getId() == accountId) {
				JSONObject receivedJson = new JSONObject(rideJson);
				
				updatedRide.setOrigin(receivedJson.getString("origin"));
				updatedRide.setDestination(receivedJson.getString("destination"));
				updatedRide.setPrice(receivedJson.getDouble("price"));
				updatedRide.setDeparture(receivedJson.getString("departure"));
				updatedRide.setArrival(receivedJson.getString("arrival"));
				
				return rideRepository.save(updatedRide);
			}
		}

		return Ride.EMPTY;
	}

	@Override
	public List<Ride> addPassenger(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
//			Account passenger = accountRepository.findByNickName(auth.getName());
			Account passenger = accountService.getAccountByNickname(auth.getName());
			Ride ride = rideRepository.getOne(id);
			
			passenger.getReservedRides().add(ride);
//			accountRepository.save(passenger);
			accountService.save(passenger);
			
//			return accountRepository.getOne(passenger.getId()).getReservedRides();
			return accountService.getAccount(passenger.getId()).getReservedRides();
		}
		
		return Collections.emptyList();
	}

	@Override
	public List<Ride> cancelRide(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
//			Account passenger = accountRepository.findByNickName(auth.getName());
			Account passenger = accountService.getAccountByNickname(auth.getName());
			Ride ride = rideRepository.getOne(id);
			
			passenger.getReservedRides().remove(ride);
//			accountRepository.save(passenger);
			accountService.save(passenger);
			
//			return accountRepository.getOne(passenger.getId()).getReservedRides();
			return accountService.getAccount(passenger.getId()).getReservedRides();
		}
		
		return Collections.emptyList();
	}

	@Override
	public Boolean deleteRide(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
//			Long accountId = accountRepository.findByNickName(auth.getName()).getId();
			Long accountId = accountService.getAccountByNickname(auth.getName()).getId();
			Ride deletedRide = rideRepository.getOne(id);
			
			if(deletedRide.getDriver().getId() == accountId) {
				rideRepository.deleteById(id);
			}
		}
		
		return !rideRepository.existsById(id);
	}
	
	private Pageable getNextPageOrderByDeparture(Integer page) {
		return PageRequest.of(page,10,Sort.by("departure").ascending());
	}

}
