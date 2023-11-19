package com.redhat.test.rest;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient
public interface RemoteServiceClient {

    @GET
    @Path("/rest/v2/caches/person/{key}")
    Person getPerson(@HeaderParam("Authorization") String authHeader,@PathParam("key") String key);

    @POST
    @Path("/rest/v2/caches/person/{key}")
    void postPerson(@HeaderParam("Authorization") String authHeader, @PathParam("key") String key, String personJson);

}
