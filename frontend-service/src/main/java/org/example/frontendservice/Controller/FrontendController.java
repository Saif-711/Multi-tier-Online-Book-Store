package org.example.frontendservice.Controller;

import org.example.frontendservice.Service.FrontendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

@RestController
@RequestMapping("/frontend")
public class FrontendController {

    private final FrontendService frontendService;

    public FrontendController(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @GetMapping("/search/{topic}")
    public ResponseEntity<?> search(@PathVariable String topic) {
        try {
            return ResponseEntity.ok(frontendService.search(topic));
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Downstream service unavailable"));
        }
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> info(@PathVariable int id) {
        try {
            return ResponseEntity.ok(frontendService.info(id));
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Downstream service unavailable"));
        }
    }

    @PostMapping("/purchase/{id}")
    public ResponseEntity<?> purchase(@PathVariable int id) {
        try {
            return frontendService.purchase(id);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "error", e.getMessage()
                    ));
        }
    }
}
