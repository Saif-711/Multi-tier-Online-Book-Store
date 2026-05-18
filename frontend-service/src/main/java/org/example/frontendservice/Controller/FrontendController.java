package org.example.frontendservice.Controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.frontendservice.Service.FrontendService;

@RestController
@RequestMapping("/")
public class FrontendController {

    private final FrontendService frontendService;

    public FrontendController(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    // SEARCH
    @GetMapping("/search/{topic}")
    public ResponseEntity<?> search(@PathVariable String topic) {
        return ResponseEntity.ok(frontendService.search(topic));
    }

    // INFO
    @GetMapping("/info/{id}")
    public ResponseEntity<?> info(@PathVariable int id) {
        return ResponseEntity.ok(frontendService.info(id));
    }

    // PURCHASE
    @PostMapping("/purchase/{id}")
    public ResponseEntity<?> purchase(@PathVariable int id) {
        return frontendService.purchase(id);
    }
}