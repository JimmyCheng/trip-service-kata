package org.craftedsw.tripservicekata.trip;

import static org.junit.Assert.*;

import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class TripServiceTest {

	private static final User GUEST = null;
	private static final User REGISTERED_USER = new User();
	private static final User UNUSED_USER = null;
	private static final User ANOTHER_USER = new User();
	private static final Trip TO_BRAZIL = new Trip();
	private static final Trip TO_LONDON = new Trip();
	private User loggedInUser;
	private TripService tripService;
  
	@Before
	public void initialize(){
		tripService = new TestableTripService();
		loggedInUser = REGISTERED_USER;
	}
	
	@Test(expected = UserNotLoggedInException.class)
	public void should_throw_exception_when_user_is_not_logged_in() {

		loggedInUser = GUEST;
		tripService.getTripsByUser(UNUSED_USER);
	}

	@Test
	public void should_not_return_any_friends_when_users_are_not_friends(){
		User friend = UserBuilder.aUser()
				      .friendWith(ANOTHER_USER)
				      .withTrips(TO_BRAZIL)
				      .build();

		List<Trip> friendTrips = tripService.getTripsByUser(friend);
		
		assertThat(friendTrips.size(),is(0));
	}
    
	@Test
	public void should_retrun_friend_trips_when_users_are_friends() throws Exception {
		TripService tripService = new TestableTripService();

		User friend = UserBuilder.aUser()
				      .friendWith(ANOTHER_USER, loggedInUser)
				      .withTrips(TO_BRAZIL, TO_LONDON)
				      .build();
		
		List<Trip> friendTrips = tripService.getTripsByUser(friend);
		
		assertThat(friendTrips.size(),is(2));		
	}
	
	public static class UserBuilder{

		private User[] friends = new User[]{};
		private Trip[] trips = new Trip[]{};

		public static UserBuilder aUser(){
			return new UserBuilder();
		}

		public User build() {
            User user = new User();
            addTripsTo(user);
            addFriendsTo(user);
            return user;
		}

		private void addFriendsTo(User user) {
            for(Trip trip:trips){
            	user.addTrip(trip);
            }
            
		}

		private void addTripsTo(User user) {
			for(User friend:friends){
				user.addFriend(friend);
			}
		}

		public UserBuilder withTrips(Trip...trips) {
            this.trips  = trips; 
			return this;
		}

		public UserBuilder friendWith(User...friends) {
            this.friends  = friends;
			return this;
		}
	}


	private class TestableTripService extends TripService {

		@Override
		protected User getLoggedUser() {
			return loggedInUser;
		}

		@Override
		protected List<Trip> tripsBy(User user) {
			return user.trips();
		}
		
	}

}
