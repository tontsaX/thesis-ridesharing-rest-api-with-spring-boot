package tontsax.kimppakyyti.services;

import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;

import tontsax.kimppakyyti.dao.Ride;

public interface RideService {
	public abstract List<Ride> getRides(Integer page);
	public abstract List<Ride> getRidesByOrigin(String origin, Integer page);
	public abstract List<Ride> getRidesByDestination(String destination, Integer page);
	public abstract List<Ride> getRidesByDeparture(String departure, Integer page);
	public abstract List<Ride> getRidesByArrival(String arrival, Integer page);
	public abstract Ride getRide(Long id);
	
	public abstract Ride createRide(String rideJson) throws JSONException;
	
	// you can merge these update methods
	public abstract Ride updateRide(String rideJson, Long id) throws JSONException;
	public abstract List<Ride> addPassenger(Long id);
	public abstract List<Ride> cancelRide(Long id);
	
	public abstract Boolean deleteRide(Long id);
}
