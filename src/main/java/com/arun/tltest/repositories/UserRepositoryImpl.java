package com.arun.tltest.repositories;

import com.arun.tltest.models.User;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

public class UserRepositoryImpl extends CustomRepositoryImpl<User> {
    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    public UserRepositoryImpl(ReactiveMongoTemplate template) {
        super(template);
    }
}
