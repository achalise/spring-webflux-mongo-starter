package com.arun.tltest.handlers;

import com.arun.tltest.models.User;
import com.arun.tltest.queryFilters.QueryFiltersService;
import com.arun.tltest.repositories.CustomRepository;
import com.arun.tltest.repositories.UserRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRequestHandler extends RequestHandler<User> {

    private UserRepository userRepository;

    public UserRequestHandler(UserRepository userRepository, QueryFiltersService queryFiltersService) {
        super(queryFiltersService);
        this.userRepository = userRepository;
    }

    @Override
    protected ReactiveMongoRepository getRepository() {
        return this.userRepository;
    }

    @Override
    protected CustomRepository getCustomRepository() {
        return this.userRepository;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
