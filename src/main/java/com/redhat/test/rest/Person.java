package com.redhat.test.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Alexander Barbosa
 */

@RegisterForReflection
public class Person{
    @JsonProperty("firstName")
    String firstName;
    
    @JsonProperty("lastName")
    String lastName;
    
    @JsonProperty("bornYear")
    int bornYear;
    
    @JsonProperty("bornIn")
    String bornIn;

    public Person(String firstName, String lastName, int bornYear, String bornIn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bornYear = bornYear;
        this.bornIn = bornIn;
    }

    @Override
    public String toString() {
        return '{' +
                " \"firstName\":\"" + firstName + '"' +
                ", \"lastName\":\"" + lastName + '"' +
                ", \"bornYear\":\"" + bornYear + '"' +
                ", \"bornIn\":\"" + bornIn + '"' +
                '}';
    }
}
