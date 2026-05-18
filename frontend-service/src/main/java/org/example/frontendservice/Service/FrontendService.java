package org.example.frontendservice.Service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Service
public class FrontendService {

    private final RestTemplate restTemplate;

    @Value("${catalog.base-url}")
    private String catalogBaseUrl;

    @Value("${order.base-url}")
    private String orderBaseUrl;

    public FrontendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // SEARCH
    public Object search(String topic) {
        String url = catalogBaseUrl + "/search/" + topic;
        return restTemplate.getForObject(url, Object.class);
    }

    // INFO
    public Object info(int id) {
        String url = catalogBaseUrl + "/info/" + id;
        return restTemplate.getForObject(url, Object.class);
    }

    // PURCHASE
    public ResponseEntity<?> purchase(int id) {
        String url = orderBaseUrl + "/purchase/" + id;
        return restTemplate.postForEntity(url, null, Object.class);
    }
}