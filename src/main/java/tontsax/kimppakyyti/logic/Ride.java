package tontsax.kimppakyyti.logic;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // siis mitä vittua??
@Data
public class Ride extends AbstractPersistable<Long> {
	
	private String origin, destination;
	private Double price;
	
}
