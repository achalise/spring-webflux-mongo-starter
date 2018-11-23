package com.arun.tltest;

import com.mongodb.MongoClient;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

public class MongoConfiguration extends AbstractMongoConfiguration {
    @Override
    public MongoClient mongoClient() {
        return null;
    }

    @Override
    protected String getDatabaseName() {
        return "localhost";
    }
}
