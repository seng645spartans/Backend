package com.crimeMap.Backend.Services.GeoCoding;

import com.crimeMap.Backend.DTO.Response.GeocodingResultDTO;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Objects;

@Service
public class GeoCodingService {

    @Value("${google.api.key}")
    private String apiKey;

    private GeoApiContext context;
    JedisPooled jedisPooled = new JedisPooled("rediss://default:AVNS_1y0tQ8vD1huNXC2q9kH@redis-2095cc86-umbc-545f.a.aivencloud.com:24482");

    @PostConstruct
    public void init() {
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    public GeocodingResultDTO geocode(String address) throws Exception {

        if (Objects.nonNull(jedisPooled.get(address+ ":lat"))){
             GeocodingResultDTO geocodingResultDTO = new GeocodingResultDTO();
             geocodingResultDTO.setLat(Double.valueOf(jedisPooled.get(address+ ":lat")));
             geocodingResultDTO.setLng(Double.valueOf(jedisPooled.get(address + ":long")));
             return geocodingResultDTO;
        }
        GeocodingResult[] geocodingResults = GeocodingApi.geocode(context, address).await();
        jedisPooled.set(address + ":lat", String.valueOf(geocodingResults[0].geometry.location.lat));
        jedisPooled.set(address + ":long", String.valueOf(geocodingResults[0].geometry.location.lng));
        return new GeocodingResultDTO(geocodingResults[0].geometry.location.lat,
                geocodingResults[0].geometry.location.lng);
    }

    @PreDestroy
    public void shutdown() {
        if (context != null) {
            context.shutdown();
        }
    }
}
