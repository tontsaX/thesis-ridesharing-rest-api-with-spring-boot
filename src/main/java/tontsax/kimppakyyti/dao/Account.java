package tontsax.kimppakyyti.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password", "registered", "reservedRides", "postedRides"})
public class Account extends AbstractPersistable<Long> {
	public static Account EMPTY = new Account();

	private String nickName;
	private String password;
	private int ranking = 5;
	private LocalDateTime registered = LocalDateTime.now();
	
	@ManyToMany
	private List<Ride> reservedRides = new ArrayList<>();
	
	@OneToMany(mappedBy = "driver")
	private List<Ride> postedRides = new ArrayList<>();
	
	@ManyToMany(mappedBy = "owners")
	private List<Conversation> conversations = new ArrayList<>();
	
	public JSONObject simplified() throws JSONException {
		return new JSONObject().put("id", this.getId())
							   .put("nickName", this.nickName)
							   .put("ranking", this.ranking);
	}
}
