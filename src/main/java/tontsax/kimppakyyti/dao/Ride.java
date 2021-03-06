package tontsax.kimppakyyti.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
public class Ride extends AbstractPersistable<Long> {
	public static Ride EMPTY = new Ride();
	
	private String origin, destination;
	private double price;
	
	private LocalDateTime created = LocalDateTime.now();
	private String departure, arrival;
	
	@ManyToOne
	private Account driver;
	
	@ManyToMany(mappedBy = "reservedRides")
	@JsonManagedReference
	private List<Account> passengers = new ArrayList<>();
	
	public Ride(String origin) {
		this.origin = origin;
	}
	
	public Ride(String origin, String destination, Double price) {
		this.origin = origin;
		this.destination = destination;
		this.price = price;
	}
	
	public List<JSONObject> simplifiedPassengers() throws JSONException {
		List<JSONObject> simplePassengers = new ArrayList<>();
		
		for(Account passenger: this.passengers) {
			simplePassengers.add(passenger.simplified());
		}
		
		return simplePassengers;
	} 
}
