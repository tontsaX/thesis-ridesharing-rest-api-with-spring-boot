package tontsax.kimppakyyti.logic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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
	
	private String origin, destination;
	private Double price;
	
	@ManyToOne
	@JsonBackReference
	private Account driver;
	
	@ManyToMany(mappedBy = "reservedRides")
	@JsonManagedReference
	private List<Account> passengers = new ArrayList<>();
	
	public Ride(String origin, String destination, Double price) {
		this.origin = origin;
		this.destination = destination;
		this.price = price;
	}
}
