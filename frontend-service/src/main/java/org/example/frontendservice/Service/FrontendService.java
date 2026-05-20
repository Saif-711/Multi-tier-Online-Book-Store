package org.example.frontendservice.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FrontendService {

    private final RestTemplate restTemplate;

    // Catalog replicas
    @Value("${catalog.urls}")
    private String catalogUrls;

    // Order replicas
    @Value("${order.urls}")
    private String orderUrls;

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
    // INFO + TIMING
    // ==========================

    public Object info(int id) {

        long start =
                System.currentTimeMillis();

        String url =
                getCatalogServer()
                        + "/info/"
                        + id;

        Object response =
                restTemplate.getForObject(
                        url,
                        Object.class
                );

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
    // PURCHASE + TIMING
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