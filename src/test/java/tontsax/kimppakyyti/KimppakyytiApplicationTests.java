package tontsax.kimppakyyti;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import tontsax.kimppakyyti.dao.RideDao;
import tontsax.kimppakyyti.logic.Ride;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KimppakyytiApplicationTests {
	
	private static boolean databasePopulated = false;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private RideDao rideRepository;
	
	@BeforeEach
	public void populateDatabase() {
		if(!databasePopulated) {
			Ride ride1 = new Ride("Turku", "Helsinki", 10.0);
			Ride ride2 = new Ride("Turku", "Tampere", 23.5);
				
			rideRepository.save(ride1);
			rideRepository.save(ride2);
			
			databasePopulated = true;
		}
	}
	
	@Test
	public void checkConnection() throws Exception {
		mockMvc.perform(get("/rides"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void responseContainsListOfAllRides() throws Exception {
		performRequestAndExpectJson(get("/rides"))
			.andExpect(jsonPath("$.length()", is(2)));
	}
	
	@Test
	public void findRideById() throws Exception {
		getCheckRide((long) 1, "Turku", "Helsinki");
		getCheckRide((long) 2, "Turku", "Tampere");
	}
	
	private void getCheckRide(Long id, String origin, String destination) throws Exception {
		
		performRequestAndExpectJson(get("/rides/{id}", id))
			.andExpect(jsonPath("$.id").value(id.toString()))	
			.andExpect(jsonPath("$.origin").value(origin))
			.andExpect(jsonPath("$.destination").value(destination));
		
		/* jsonPath(olioviite, is(vertausarvo)) vertaa vertausarvoa olioviitteeseen juuri sellaisena kuin vertausarvo on,
		 * paitsi numeroiden kohdalla, jolloin long 1 tai 1L eivät ole sama, kuin olioviitearvo 1.
		 * olioviitearvo int 1 ei ole long 1L tai string "1", mutta value()-matcherin avulla olioviitearvoa ja vertausarvoa
		 * voidaan verrata stringeinä. 
		 * olioviitearvo int 1 onkin string "1" ja vertausarvo 1L on string "1" 
		 * */
	}
	
	private ResultActions performRequestAndExpectJson(MockHttpServletRequestBuilder request) throws Exception {
		return mockMvc.perform(request)
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
}
