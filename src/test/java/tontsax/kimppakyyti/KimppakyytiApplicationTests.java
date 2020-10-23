package tontsax.kimppakyyti;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import tontsax.kimppakyyti.dao.Account;
import tontsax.kimppakyyti.dao.AccountDao;
import tontsax.kimppakyyti.dao.Ride;
import tontsax.kimppakyyti.dao.RideDao;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KimppakyytiApplicationTests {
	
	private static boolean databasePopulated = false;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private RideDao rideRepository;
	
	@Autowired
	private AccountDao accountRepository;
	
	private static Account account1, account2;
	
	private ResultActions mvcResultActions;
	
	@BeforeEach
	public void populateDatabase() {
		if(!databasePopulated) {
			
			account1 = new Account();
			account2 = new Account();
			
			account1.setNickName("Decimus");
			account2.setNickName("Tilemar");
			
			accountRepository.save(account1);
			accountRepository.save(account2);
			
			Ride ride1 = new Ride("Turku", "Helsinki", 10.0);
			Ride ride2 = new Ride("Turku", "Tampere", 23.5);
			
			LocalDateTime departure1 = LocalDateTime.of(2020, 9, 22, 14, 14);
			LocalDateTime departure2 = LocalDateTime.of(2020, 9, 22, 13, 14);
			LocalDateTime arrival1 = LocalDateTime.of(2020, 9, 23, 15, 15);
			LocalDateTime arrival2 = LocalDateTime.of(2020, 9, 23, 14, 15);
			
			ride1.setDeparture(departure1.toString());
			ride1.setArrival(arrival1.toString());
			ride1.setDriver(accountRepository.getOne(1L));
			
			ride2.setDriver(accountRepository.getOne(2L));
			ride2.setDeparture(departure2.toString());
			ride2.setArrival(arrival2.toString());

			rideRepository.save(ride1);
			rideRepository.save(ride2);
			
			databasePopulated = true;
		}
	}
	
	@Test
	@Order(1)
	public void checkConnection() throws Exception {
		mockMvc.perform(get("/rides"))
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(2)
	public void getListOfAllRides() throws Exception {
		mvcResultActions = performJsonRequestAllRidesAndExpectJson();
		checkRidesListLength(2);
	}
	
	@Test
	@Order(3)
	public void getRidesByOrigin() throws Exception {
		mvcResultActions = performJsonRequestAndExpectJson(get("/rides/from{origin}", "Turku"))
								.andExpect(jsonPath("$.length()", is(2)))
								.andExpect(jsonPath("$[0].departure").value("2020-09-22T13:14"));
	}
	
	@Test
	@Order(4)
	public void getRidesByDestination() throws Exception {
		mvcResultActions = performJsonRequestAndExpectJson(get("/rides/to{destination}", "Helsinki"))
								.andExpect(jsonPath("$.length()", is(1)));
	}
	
	@Test
	@Order(13)
	public void getRidesByDepartureAndArrival() throws Exception {
		String departure = LocalDateTime.of(2020, 7, 3, 13, 55).toString();
		String arrival = LocalDateTime.of(2020, 9, 3, 14, 25).toString();
		
		// ride with id 3 has been updated at this point
		mvcResultActions = performJsonRequestAndExpectJson(get("/rides/departure").param("departure", departure))
								.andExpect(jsonPath("$.length()", is(1)))
								.andExpect(jsonPath("$[0].id").value(3L))
								.andExpect(jsonPath("$[0].departure").value(departure));
		
		mvcResultActions = performJsonRequestAndExpectJson(get("/rides/arrival").param("arrival", arrival))
								.andExpect(jsonPath("$.length()", is(1)))
								.andExpect(jsonPath("$[0].id").value(3L))
								.andExpect(jsonPath("$[0].arrival").value(arrival));
	}
	
	@Test
	@Order(14)
	public void getRidesOfTheSecondPage() throws Exception {
		addRidesToDatabase();
		mvcResultActions = performJsonRequestAndExpectJson(get("/rides").param("page", "1"));
		
		checkRidesListLength(4);
	}
	
	private void addRidesToDatabase() throws Exception {
		for(int i = 0; i < 12; i++) {
			Ride ride = new Ride("Oulu", "Tampere", 30.0);
			ride.setDeparture(LocalDateTime.of(2020,9,12,14,14).toString());
			ride.setArrival(LocalDateTime.of(2020,9,13,00,14).toString());
			
			rideRepository.save(ride);
		}
	}
	
	@Test
	@Order(5)
	public void getRideById() throws Exception {
		requestRideById(3L, "Turku", "Helsinki", "2020-09-22T14:14", "2020-09-23T15:15");
		requestRideById(4L, "Turku", "Tampere", "2020-09-22T13:14", "2020-09-23T14:15");
	}
	
	private void requestRideById(Long id, String origin, String destination,
								 	String departure, String arrival) throws Exception {
		mvcResultActions = performJsonRequestAndExpectJson(get("/rides/{id}", id))
								.andExpect(jsonPath("$.id").value(id.toString()))	
								.andExpect(jsonPath("$.origin").value(origin))
								.andExpect(jsonPath("$.destination").value(destination))
								.andExpect(jsonPath("$.departure").value(departure))
								.andExpect(jsonPath("$.arrival").value(arrival));
	}
	
	@Test
	@Order(6)
	public void postRide() throws Exception {
		String departure = LocalDateTime.of(2020, 8, 24, 18, 45).toString();
		String arrival = LocalDateTime.of(2020, 12, 25, 14, 25).toString();

		JSONObject jsonRide = createJsonRide("Tampere", "Oulu",
												   25.0, account1.getId(),
												   departure, arrival);
		
		mvcResultActions = performJsonRequestAndExpectJson(post("/rides")
//								.with(csrf())
								.content(jsonRide.toString()).with(csrf()))
								.andExpect(jsonPath("$.origin").value("Tampere"))
								.andExpect(jsonPath("$.destination").value("Oulu"))
								.andExpect(jsonPath("$.departure").value(departure))
								.andExpect(jsonPath("$.arrival").value(arrival));
		
		mvcResultActions = performJsonRequestAllRidesAndExpectJson();
		checkRidesListLength(3);
		checkDepartureAndArrival(0, departure, arrival);
	}
	
	@Test
	@Order(7)
	public void deleteRideById() throws Exception {
		MvcResult request = performJsonRequestAndExpectJson(delete("/rides/{id}", 4L).with(csrf())).andReturn();
		String content = request.getResponse().getContentAsString();
		
		Assert.assertTrue(content.equalsIgnoreCase("true"));
		
		mvcResultActions = performJsonRequestAllRidesAndExpectJson();
		checkRidesListLength(2);
	}
	
	@Test
	@Order(8)
	public void updateRide() throws Exception {
		String departure = LocalDateTime.of(2020,7,3,13,55).toString();
		String arrival = LocalDateTime.of(2020,9,3,14,25).toString();
		
		JSONObject jsonRide = createJsonRide("Turku", "Oulu",
											 25.0, 0L,
											 departure, arrival);
		
		mvcResultActions = performJsonRequestAndExpectJson(put("/rides/{id}", 3L)
								.with(csrf())
								.content(jsonRide.toString()))
								.andExpect(jsonPath("$.id").value("3"))
								.andExpect(jsonPath("$.origin").value("Turku"))
								.andExpect(jsonPath("$.destination").value("Oulu"))
								.andExpect(jsonPath("$.price").value("25.0"))
								.andExpect(jsonPath("$.departure").value(departure))
								.andExpect(jsonPath("$.arrival").value(arrival));
	}
	
	@Test
	@Order(9)
	public void registerToApp() throws Exception {
		JSONObject newDriver = new JSONObject();
		newDriver.put("nickName", "Tontsa");
		newDriver.put("password", "Salasana");
		
		mvcResultActions = performJsonRequestAndExpectJson(post("/register")
								.with(csrf())
								.content(newDriver.toString()))
								.andExpect(jsonPath("$.id").value("6"))
								.andExpect(jsonPath("$.nickName").value("Tontsa"));
	}
	
//	
//	@Test
//	@Order(10)
//	public void loginToAccount() throws Exception {
//		
//	}
//	
//	@Test
//	@Order(11)
//	public void hopOnARide() throws Exception {
//		
//	}
//	
//	@Test
//	@Order(12)
//	public void cancelARide() throws Exception {
//		
//	}
	
	private ResultActions performJsonRequestAllRidesAndExpectJson() throws Exception {
		return performJsonRequestAndExpectJson(get("/rides"));
	}
	
	private ResultActions performJsonRequestAndExpectJson(MockHttpServletRequestBuilder request) throws Exception {
		return mockMvc.perform(request)
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	private void checkRidesListLength(int length) throws Exception {
		mvcResultActions
		.andExpect(jsonPath("$.numberOfElements", is(length)));
	}
	
	private void checkDepartureAndArrival(int index, String departure, String arrival) throws Exception {
		mvcResultActions
		.andExpect(jsonPath("$.content[" + index + "].departure").value(departure))
		.andExpect(jsonPath("$.content[" + index + "].arrival").value(arrival));
	}
	
	private static JSONObject createJsonRide(String origin, String destination,
									  double price, long driverId,
									  String departure, String arrival) throws JSONException {
		JSONObject jsonRideObject = new JSONObject();
		
		jsonRideObject.put("origin", origin);
		jsonRideObject.put("destination", destination);
		jsonRideObject.put("price", price);
		jsonRideObject.put("driverId", driverId);
		jsonRideObject.put("departure", departure);
		jsonRideObject.put("arrival", arrival);
		
		return jsonRideObject;
	}
}
