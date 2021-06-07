package tontsax.kimppakyyti.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Conversation extends AbstractPersistable<Long> {
	public static Conversation EMPTY = new Conversation();
	
	@ManyToOne
//	@JoinColumn(name = "owner_id")
	@JsonIgnoreProperties({"password", "ranking", "registered", "reservedRides", "postedRides", "conversations"})
	private Account owner;
	
	@ManyToMany
	@JsonIgnoreProperties({"password", "ranking", "registered", "reservedRides", "postedRides", "conversations"})
	private List<Account> owners = new ArrayList<>();
	
	@OneToMany(mappedBy = "conversation")
	@JsonIgnoreProperties({"conversation"})
	private List<Message> messages = new ArrayList<>();
}
