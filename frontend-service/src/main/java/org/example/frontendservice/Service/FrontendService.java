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

    // Catalog replicas
    @Value("${catalog.urls}")
    private String catalogUrls;

    // Order replicas
    @Value("${order.urls}")
    private String orderUrls;

    // Cache
    private final Map<Integer, Object> cache = new ConcurrentHashMap<>();

    // Round Robin counters
    private int catalogIndex = 0;
    private int orderIndex = 0;

    public FrontendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ==========================
    // ROUND ROBIN HELPERS
    // ==========================

    private String getCatalogServer() {

        String[] urls = catalogUrls.split(",");

        String selected = urls[catalogIndex];

        catalogIndex = (catalogIndex + 1) % urls.length;

        System.out.println("Using Catalog Server: " + selected);

        return selected;
    }

    private String getOrderServer() {

        String[] urls = orderUrls.split(",");

        String selected = urls[orderIndex];

        orderIndex = (orderIndex + 1) % urls.length;

        System.out.println("Using Order Server: " + selected);

        return selected;
    }

    // ==========================
    // SEARCH
    // ==========================

    public Object search(String topic) {

        String url = getCatalogServer()
                + "/search/" + topic;

        return restTemplate.getForObject(url, Object.class);
    }

    // ==========================
    // INFO + CACHE
    // ==========================

    public Object info(int id) {

        // cache hit
        if (cache.containsKey(id)) {

            System.out.println("Cache HIT for book " + id);

            return cache.get(id);
        }

        System.out.println("Cache MISS for book " + id);

        String url = getCatalogServer()
                + "/info/" + id;

        Object response =
                restTemplate.getForObject(url, Object.class);

        cache.put(id, response);

        return response;
    }

    // ==========================
    // PURCHASE + INVALIDATE CACHE
    // ==========================

    public ResponseEntity<?> purchase(int id) {

        String url = getOrderServer()
                + "/purchase/" + id;

        ResponseEntity<?> response =
                restTemplate.postForEntity(
                        url,
                        null,
                        Object.class
                );

        cache.remove(id);

        System.out.println(
                "Cache invalidated for book " + id
        );

        return response;
    }
}