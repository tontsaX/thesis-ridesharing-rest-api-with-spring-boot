package tontsax.kimppakyyti.logic;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ride extends AbstractPersistable<Long> {

}
