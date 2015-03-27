package com.hubspot.dropwizard.guice.sample.db;

//This is to test Just In Time binding.
//This class should be available to Resources without an explicit binding statement.
public class JitDAO {
    public String getMessage() {
        return "this DAO was bound just-in-time";
    }
}
