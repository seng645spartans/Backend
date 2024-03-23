package com.crimeMap.Backend.Services.GeoCoding;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class GeoCodingService {

    @Value("${google.api.key}")
    private String apiKey;

    private GeoApiContext context;

    @PostConstruct
    public void init() {
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    public GeocodingResult[] geocode(String address) throws Exception {
        //System.out.println("Address : " +address);
        return GeocodingApi.geocode(context, address).await();
    }

    @PreDestroy
    public void shutdown() {
        if (context != null) {
            context.shutdown();
        }
    }
}
