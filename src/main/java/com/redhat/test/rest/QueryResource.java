package com.redhat.test.rest;

import java.util.Base64;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/query")
@RequestScoped
public class QueryResource {

    @Inject
    @RestClient
    RemoteServiceClient remoteServiceClient;
 
    @Inject
    @ConfigProperty(name = "dgserver.username")
    String username;

    @Inject
    @ConfigProperty(name = "dgserver.password")
    String password;

    @Inject
    ObjectMapper objectMapper;

    @Path("/getData/{key}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String queryGET(@PathParam("key") String key) {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        Person result = remoteServiceClient.getPerson(authHeader, key);
        return "GET data from DG cache Person "+result.toString();
    }

    @Path("/putData/{key}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String queryPOST(Person person, @PathParam("key") String key) throws JsonProcessingException {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        String personJson = objectMapper.writeValueAsString(person);
        System.out.println("Convert Person POJO to JSON: "+personJson);
        remoteServiceClient.postPerson(authHeader,key,personJson);
        return "POST data in DG cache";
    }

}
