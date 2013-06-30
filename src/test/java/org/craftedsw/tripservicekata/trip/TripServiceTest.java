package org.craftedsw.tripservicekata.trip;

import static org.junit.Assert.*;

import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.craftedsw.tripservicekata.trip.UserBuilder.*;

public class TripServiceTest {

	private static final User GUEST = null;
	private static final User REGISTERED_USER = new User();
	private static final User UNUSED_USER = null;
	private static final User ANOTHER_USER = new User();
	private static final Trip TO_BRAZIL = new Trip();
	private static final Trip TO_LONDON = new Trip();
	private TripService tripService;
  
	@Before
	public void initialize(){
		tripService = new TestableTripService();
	}
	
	@Test(expected = UserNotLoggedInException.class)
	public void should_throw_exception_when_user_is_not_logged_in() {

		tripService.getTripsByUser(UNUSED_USER, null);
	}

	@Test
	public void should_not_return_any_friends_when_users_are_not_friends(){
		User friend = aUser()
				      .friendWith(ANOTHER_USER)
				      .withTrips(TO_BRAZIL)
				      .build();

		List<Trip> friendTrips = tripService.getTripsByUser(friend, REGISTERED_USER);
		
		assertThat(friendTrips.size(),is(0));
	}
    
	@Test
	public void should_retrun_friend_trips_when_users_are_friends() throws Exception {
		TripService tripService = new TestableTripService();

		User friend = aUser()
				      .friendWith(ANOTHER_USER, REGISTERED_USER)
				      .withTrips(TO_BRAZIL, TO_LONDON)
				      .build();
		
		List<Trip> friendTrips = tripService.getTripsByUser(friend, REGISTERED_USER);
		
		assertThat(friendTrips.size(),is(2));		
	}
	
	private class TestableTripService extends TripService {



		@Override
		protected List<Trip> tripsBy(User user) {
			return user.trips();
		}
		
	}

}
