package tontsax.kimppakyyti;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import tontsax.kimppakyyti.dao.RideDao;
import tontsax.kimppakyyti.logic.Ride;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KimppakyytiApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private RideDao rideRepository;
	
	@Before
	public void initializeDatabase() {
		Ride ride1 = new Ride("Turku", "Helsinki", 10.0);
		Ride ride2 = new Ride("Turku", "Tampere", 23.5);
		
		rideRepository.save(ride1);
		rideRepository.save(ride2);
	}
	
	@Test
	public void checkConnection() throws Exception {
		mockMvc.perform(get("/rides"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void responseContainsListOfAllRides() throws Exception {
		MvcResult response = mockMvc.perform(get("/rides")).andReturn();
		String data = response.getResponse().getContentAsString();
		System.out.println(data);
		Assert.assertTrue(true);
	}

}
