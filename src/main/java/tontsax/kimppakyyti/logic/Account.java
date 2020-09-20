package tontsax.kimppakyyti.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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
public class Account extends AbstractPersistable<Long> {

	private String nickName;
	private int rankingFive;
	private LocalDateTime registered;
	
	@ManyToMany
	@JsonBackReference
	private List<Ride> reservedRides = new ArrayList<>();
	
	@OneToMany(mappedBy = "driver")
	@JsonManagedReference
	private List<Ride> postedRides = new ArrayList<>();
}
