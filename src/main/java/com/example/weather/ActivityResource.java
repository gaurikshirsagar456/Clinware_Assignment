package com.example.weather;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/activity")
public class ActivityResource {

    @Inject
    ActivityService activityService;

    @ConfigProperty(name = "weather-api/mp-rest/url")
    String weatherUrl;

    @ConfigProperty(name = "mcp-api/mp-rest/url")
    String mcpUrl;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRecommendation(
            @QueryParam("lat") double lat,
            @QueryParam("lon") double lon) {

        return activityService.getRecommendation(lat, lon);
    }

    @GET
    @Path("/status")
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {

        if (weatherUrl != null && !weatherUrl.isBlank()
                && mcpUrl != null && !mcpUrl.isBlank()) {
            return "READY";
        }

        return "NOT_CONFIGURED";
    }
}