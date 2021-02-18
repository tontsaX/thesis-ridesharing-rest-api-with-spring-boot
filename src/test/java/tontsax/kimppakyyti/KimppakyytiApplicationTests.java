package tontsax.kimppakyyti;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
	private static Account account1, account2;
	private static String accountAddress = "/account";
	private static String ridesAddress = "/rides";
	
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private RideDao rideRepository;
	
	@Autowired
	private AccountDao accountRepository;
	
	private ResultActions mvcResultActions;
	
	@BeforeEach
	public void populateDatabase() {
		if(!databasePopulated) {
			
			account1 = new Account();
			account2 = new Account();
			
			account1.setNickName("Tiberius");
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			account1.setPassword(passwordEncoder.encode("password"));
			accountRepository.save(account1);
			
			account2.setNickName("Augustus");
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
		
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
	}
	
	@Test
	@Order(1)
	public void checkConnection() throws Exception {
//		mockMvc.perform(get("/rides"))
		mockMvc.perform(get(ridesAddress))
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
		mvcResultActions = performJsonRequestAndExpectJson(get(ridesAddress + "/from{origin}", "Turku"))
								.andExpect(jsonPath("$.length()", is(2)))
								.andExpect(jsonPath("$[0].departure").value("2020-09-22T13:14"));
	}
	
	@Test
	@Order(4)
	public void getRidesByDestination() throws Exception {
		mvcResultActions = performJsonRequestAndExpectJson(get(ridesAddress + "/to{destination}", "Helsinki"))
								.andExpect(jsonPath("$.length()", is(1)));
	}
	
	@Test
	@Order(13)
	public void getRidesByDepartureAndArrival() throws Exception {
		String departure = LocalDateTime.of(2020, 7, 3, 13, 55).toString();
		String arrival = LocalDateTime.of(2020, 9, 3, 14, 25).toString();
		
		// ride with id 5 has been updated at this point
		mvcResultActions = performJsonRequestAndExpectJson(get(ridesAddress + "/departure").param("departure", departure))
								.andExpect(jsonPath("$.length()", is(1)))
								.andExpect(jsonPath("$[0].id").value(5L))
								.andExpect(jsonPath("$[0].departure").value(departure));
		
		mvcResultActions = performJsonRequestAndExpectJson(get(ridesAddress + "/arrival").param("arrival", arrival))
								.andExpect(jsonPath("$.length()", is(1)))
								.andExpect(jsonPath("$[0].id").value(5L))
								.andExpect(jsonPath("$[0].arrival").value(arrival));
	}
	
	@Test
	@Order(14)
	public void getRidesOfTheSecondPage() throws Exception {
		addRidesToDatabase();
		mvcResultActions = performJsonRequestAndExpectJson(get(ridesAddress).param("page", "1"));
		
		checkRidesListLength(4);
	}
	
	@Test
	@Order(5)
	public void getRideById() throws Exception {
		requestRideById(3L, "Turku", "Helsinki", "2020-09-22T14:14", "2020-09-23T15:15");
		requestRideById(4L, "Turku", "Tampere", "2020-09-22T13:14", "2020-09-23T14:15");
	}
	
	@Test
	@Order(6)
	public void postRide() throws Exception {
		String departure = LocalDateTime.of(2020, 8, 24, 18, 45).toString();
		String arrival = LocalDateTime.of(2020, 12, 25, 14, 25).toString();

		JSONObject jsonRide = createJsonRide("Tampere", "Oulu",
											 	25.0, departure, arrival);
		
		mvcResultActions = performJsonRequestAndExpectJson(post("/rides")
									.content(jsonRide.toString())
									.with(testAccount()) // user does not need to exists
									.with(csrf()))
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
		Assert.assertTrue(deleteRide(3L).equalsIgnoreCase("true")); // is posted ride by the test account
		Assert.assertTrue(deleteRide(4L).equalsIgnoreCase("false")); // this ride is not test account's
		
		mvcResultActions = performJsonRequestAllRidesAndExpectJson();
		checkRidesListLength(2);
	}
	
	@Test
	@Order(8)
	public void updateRide() throws Exception {
		String departure = LocalDateTime.of(2020,7,3,13,55).toString();
		String arrival = LocalDateTime.of(2020,9,3,14,25).toString();
		
		JSONObject jsonRide = createJsonRide("Turku", "Oulu",
											 25.0, departure, arrival);
		
		mvcResultActions = performJsonRequestAndExpectJson(put(accountAddress + ridesAddress + "/{id}", 5L)
								.with(testAccount())
								.with(csrf())
								.content(jsonRide.toString()))
								.andExpect(jsonPath("$.id").value("5"))
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
		newDriver.put("nickName", "Claudius");
		newDriver.put("password", "Salasana");
		
		performJsonRequestAndExpectJson(post("/register").with(csrf()).content(newDriver.toString()))
		.andReturn();
		
		mockMvc.perform(formLogin("/login")
						.user(newDriver.getString("nickName")).password(newDriver.getString("password")))
			   .andExpect(authenticated().withUsername(newDriver.getString("nickName")));
	}
	
	@Test
	@Order(11)
	public void hopOnARide() throws Exception {
		//returns account's list of reserved rides. A ride contains a list of passengers.
		mockMvc.perform(put(ridesAddress + "/{id}", 5L)
						.with(user("Claudius")).with(csrf()))
			   .andDo(print())
			   .andExpect(jsonPath("$[0].id").value("5"))
			   .andExpect(jsonPath("$[0].passengers[0].nickName").value("Claudius"));
		
		mockMvc.perform(put(ridesAddress + "/{id}", 5L)
						.with(user(account2.getNickName())).with(csrf()))
				.andReturn();
		
		//gets a ride by id and checks the passenger list
		mockMvc.perform(get(ridesAddress + "/{id}", 5L))
				.andDo(print())
				.andExpect(jsonPath("$.passengers[0].nickName").value("Claudius"))
				.andExpect(jsonPath("$.passengers[1].nickName").value(account2.getNickName()));
	}
	
	@Test
	@Order(12)
	public void cancelARide() throws Exception {
		//removes the only ride from Claudius ride list and checks the length of the returned ride list
		mockMvc.perform(put(ridesAddress + "/{id}/cancel", 5L)
							.with(user("Claudius")).with(csrf()))
				.andExpect(jsonPath("$.length()", is(0)));
		
		//gets a ride by id and checks the passenger list
		mockMvc.perform(get(ridesAddress + "/{id}", 5L))
				.andDo(print())
				.andExpect(jsonPath("$.passengers[0].nickName").value(account2.getNickName()));
	}
	
	private void addRidesToDatabase() throws Exception {
		for(int i = 0; i < 12; i++) {
			Ride ride = new Ride("Oulu", "Tampere", 30.0);
			ride.setDeparture(LocalDateTime.of(2020,9,12,14,14).toString());
			ride.setArrival(LocalDateTime.of(2020,9,13,00,14).toString());
			
			rideRepository.save(ride);
		}
	}
	
	private void requestRideById(Long id, String origin, String destination,
		 	String departure, String arrival) throws Exception {

		mvcResultActions = performJsonRequestAndExpectJson(get(ridesAddress + "/{id}", id))
				.andExpect(jsonPath("$.id").value(id.toString()))	
				.andExpect(jsonPath("$.origin").value(origin))
				.andExpect(jsonPath("$.destination").value(destination))
				.andExpect(jsonPath("$.departure").value(departure))
				.andExpect(jsonPath("$.arrival").value(arrival));
	}
	
	private ResultActions performJsonRequestAllRidesAndExpectJson() throws Exception {
		return performJsonRequestAndExpectJson(get(ridesAddress));
	}
	
	private ResultActions performJsonRequestAndExpectJson(MockHttpServletRequestBuilder request) throws Exception {
		return mockMvc.perform(request)
				.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	private String deleteRide(Long rideId) throws Exception {
		MvcResult request = performJsonRequestAndExpectJson(delete(accountAddress + "/rides/{id}", rideId)
															.with(testAccount())
															.with(csrf()))
							.andReturn();

		return request.getResponse().getContentAsString();
	}
	
	private void checkRidesListLength(int length) throws Exception {
		mvcResultActions
			.andExpect(jsonPath("$.length()", is(length)));
	}
	
	private void checkDepartureAndArrival(int rideIndex, String departure, String arrival) throws Exception {
		mvcResultActions
			.andExpect(jsonPath("$[" + rideIndex + "].departure").value(departure))
			.andExpect(jsonPath("$[" + rideIndex + "].arrival").value(arrival));
	}
	
	private static JSONObject createJsonRide(String origin, String destination,
									  			double price, String departure, String arrival) throws JSONException {
		
		JSONObject jsonRideObject = new JSONObject();
		
		jsonRideObject.put("origin", origin);
		jsonRideObject.put("destination", destination);
		jsonRideObject.put("price", price);
		jsonRideObject.put("departure", departure);
		jsonRideObject.put("arrival", arrival);
		
		return jsonRideObject;
	}
	
	private static RequestPostProcessor testAccount() {
		return user(account1.getNickName()).password("password");
	}
}
