package org.example.frontendservice.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FrontendService {

    private final RestTemplate restTemplate;

    @Value("${catalog.base-url}")
    private String catalogBaseUrl;

    @Value("${order.base-url}")
    private String orderBaseUrl;

    // 🔥 Cache
    private final Map<Integer, Object> cache = new ConcurrentHashMap<>();

    public FrontendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 🔵 SEARCH
    public Object search(String topic) {
        String url = catalogBaseUrl + "/search/" + topic;
        return restTemplate.getForObject(url, Object.class);
    }

    // 🔵 INFO + CACHE
    public Object info(int id) {

        // 🔥 Check cache
        if (cache.containsKey(id)) {
            System.out.println("Cache HIT for book " + id);
            return cache.get(id);
        }

        System.out.println("Cache MISS for book " + id);

        String url = catalogBaseUrl + "/info/" + id;
        Object response = restTemplate.getForObject(url, Object.class);

        // 🔥 Save in cache
        cache.put(id, response);

        return response;
    }

    // 🔵 PURCHASE + INVALIDATE CACHE
    public ResponseEntity<?> purchase(int id) {

        String url = orderBaseUrl + "/purchase/" + id;

        ResponseEntity<?> response =
                restTemplate.postForEntity(url, null, Object.class);

        // 🔥 invalidate cache
        cache.remove(id);
        System.out.println("Cache invalidated for book " + id);

        return response;
    }
}