package dos.project.Service;

import dos.project.Client.BookNotFoundException;
import dos.project.Client.CatalogClient;
import dos.project.Client.CatalogServiceException;
import dos.project.Model.CatalogBookInfo;
import dos.project.Repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final CatalogClient catalogClient;
    private final OrderRepository orderRepository;

    public OrderService(CatalogClient catalogClient, OrderRepository orderRepository) {
        this.catalogClient = catalogClient;
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<Map<String, String>> purchase(int id) {
        CatalogBookInfo info;
        try {
            info = catalogClient.getInfo(id);
        } catch (BookNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        } catch (CatalogServiceException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", ex.getMessage()));
        }

        if (info == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Book not found: " + id));
        }

        if (info.getQuantity() < 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Item out of stock: " + info.getTitle()));
        }

        CatalogBookInfo updated;
        try {
            updated = catalogClient.decrementStock(id);
        } catch (CatalogServiceException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Unable to complete purchase: " + ex.getMessage()));
        }

        if (updated == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Item out of stock"));
        }

        orderRepository.save(id, updated.getTitle());
        String message = "bought book " + updated.getTitle();
        log.info(message);
        System.out.println(message);

        return ResponseEntity.ok(Map.of("message", message));
    }
}
