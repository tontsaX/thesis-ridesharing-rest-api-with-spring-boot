package tontsax.kimppakyyti.services;

import java.util.List;

import tontsax.kimppakyyti.dao.Ride;

public interface RideService {
	public abstract List<Ride> getRides(Integer page);
	public abstract List<Ride> getRidesByOrigin(String origin, Integer page);
	public abstract List<Ride> getRidesByDestination(String destination, Integer page);
	public abstract List<Ride> getRidesByDeparture(String departure, Integer page);
	public abstract List<Ride> getRidesByArrival(String arrival, Integer page);
	public abstract Ride getRide(Long id);
}
