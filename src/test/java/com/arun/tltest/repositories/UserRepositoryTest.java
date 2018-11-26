package com.arun.tltest.repositories;

import com.arun.tltest.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Because of lack of time, tests not written for all methods in UserRepository, as the custom methods are
 * tested in EventRepositoryTest.
 */

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFind() {
        Mono<User> savedUser = userRepository.save(User.builder()._id("test1").user("user1").workstation("123").build());
        StepVerifier.create(savedUser.thenMany(userRepository.findAll()))
                .as("Saved user should be found on subsequent query")
                .expectNextMatches(u -> u.get_id().equals("test1"))
                .verifyComplete();
    }
}