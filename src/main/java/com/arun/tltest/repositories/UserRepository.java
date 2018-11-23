package com.arun.tltest.repositories;

import com.arun.tltest.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>, CustomRepository<User> {
}
