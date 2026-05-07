package dos.project.Controller;


import dos.project.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    public  OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/purchase/{id}")
    public ResponseEntity<?> purchase(@PathVariable int id) {
       return orderService.purchase(id);
    }
}
