package dos.catalogserver.Controller;

import dos.catalogserver.Model.BookInfoResult;
import dos.catalogserver.Model.BookSearchResult;
import dos.catalogserver.Model.UpdateRequest;
import dos.catalogserver.Service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
    @Autowired
    private CatalogService service;

    @GetMapping("/search/{topic}")
    public List<BookSearchResult> search(@PathVariable String topic) {
        return service.findByTopic(topic);
    }

    @GetMapping("/info/{id}")
    public BookInfoResult info(@PathVariable int id) {
        return service.findById(id);
    }

    @PutMapping("/update/{id}")
    public BookInfoResult update(@PathVariable int id, @RequestBody UpdateRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/decrement/{id}")
    public BookInfoResult decrement(@PathVariable int id) {
        return service.decrementStock(id);
    }
}
