package com.example.jakartanameserver;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/name")
public class NameResource {
    @GET
    @Produces("text/plain")
    public String name() {
        return "Nazar Stepanenko";
    }
}