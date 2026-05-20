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
    private final Map<Integer, Object> cache =
            new ConcurrentHashMap<>();

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

        String[] urls =
                catalogUrls.split(",");

        String selected =
                urls[catalogIndex].trim();

        catalogIndex =
                (catalogIndex + 1)
                        % urls.length;

        System.out.println(
                "Using Catalog Server: "
                        + selected
        );

        return selected;
    }

    private String getOrderServer() {

        String[] urls =
                orderUrls.split(",");

        String selected =
                urls[orderIndex].trim();

        orderIndex =
                (orderIndex + 1)
                        % urls.length;

        System.out.println(
                "Using Order Server: "
                        + selected
        );

        return selected;
    }

    // ==========================
    // SEARCH
    // ==========================

    public Object search(String topic) {

        long start =
                System.currentTimeMillis();

        String url =
                getCatalogServer()
                        + "/search/"
                        + topic;

        Object response =
                restTemplate.getForObject(
                        url,
                        Object.class
                );

        long end =
                System.currentTimeMillis();

        System.out.println(
                "Search response time: "
                        + (end - start)
                        + " ms"
        );

        return response;
    }

    // ==========================
    // INFO + CACHE + TIMING
    // ==========================

    public Object info(int id) {

        long start =
                System.currentTimeMillis();

        // Cache HIT
        if (cache.containsKey(id)) {

            System.out.println(
                    "Cache HIT for book "
                            + id
            );

            Object result =
                    cache.get(id);

            long end =
                    System.currentTimeMillis();

            System.out.println(
                    "Response time: "
                            + (end - start)
                            + " ms"
            );

            return result;
        }

        // Cache MISS
        System.out.println(
                "Cache MISS for book "
                        + id
        );

        String url =
                getCatalogServer()
                        + "/info/"
                        + id;

        Object response =
                restTemplate.getForObject(
                        url,
                        Object.class
                );

        cache.put(id, response);

        long end =
                System.currentTimeMillis();

        System.out.println(
                "Response time: "
                        + (end - start)
                        + " ms"
        );

        return response;
    }

    // ==========================
    // PURCHASE + INVALIDATION
    // + TIMING
    // ==========================

    public ResponseEntity<?> purchase(int id) {

        long start =
                System.currentTimeMillis();

        String url =
                getOrderServer()
                        + "/purchase/"
                        + id;

        ResponseEntity<?> response =
                restTemplate.postForEntity(
                        url,
                        null,
                        Object.class
                );

        // invalidate cache
        cache.remove(id);

        System.out.println(
                "Cache invalidated for book "
                        + id
        );

        long end =
                System.currentTimeMillis();

        System.out.println(
                "Purchase response time: "
                        + (end - start)
                        + " ms"
        );

        return response;
    }
}