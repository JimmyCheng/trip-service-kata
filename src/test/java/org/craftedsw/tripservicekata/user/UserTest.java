package org.craftedsw.tripservicekata.user;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.craftedsw.tripservicekata.trip.UserBuilder;
import org.junit.Test;

public class UserTest {
    private static final User BOB = new User();
    private static final User ALLEN = new User();

    @Test
    public void should_inform_when_user_are_not_friends() {
        User user = UserBuilder.aUser().friendWith(BOB).build();
        assertThat(user.isFriendWith(ALLEN), is(false));

    }
    @Test
    public void should_inform_when_user_are_friends() {
        User user = UserBuilder.aUser().friendWith(BOB, ALLEN).build();
        assertThat(user.isFriendWith(ALLEN), is(true));

    }

}
