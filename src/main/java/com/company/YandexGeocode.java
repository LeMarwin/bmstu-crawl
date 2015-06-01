package com.company;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
public interface YandexGeocode {
    @GET
    @Path("")
    @Produces("application/json")
    @Consumes("application/json")
    String getGeoinfo(@QueryParam("geocode") String geocode, @QueryParam("format") String format);
}

