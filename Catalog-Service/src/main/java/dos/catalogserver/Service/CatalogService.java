package dos.catalogserver.Service;

import dos.catalogserver.Exception.BookNotFoundException;
import dos.catalogserver.Model.*;
import dos.catalogserver.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {
    @Autowired
    private BookRepository bookRepository;

    public List<BookSearchResult> findByTopic(String topic) {
        if(bookRepository.findByTopic(topic).isEmpty()) {
            throw new BookNotFoundException("No books found for topic: " + topic);
        }
        return bookRepository.findByTopic(topic).stream()
                .map(b -> new BookSearchResult(b.getId(), b.getTitle()))
                .toList();
    }

    public BookInfoResult findById(int id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found: " + id));
        return toInfoResult(book);
    }

    public BookInfoResult update(int id, UpdateRequest request) {
        if (request == null
                || (request.getQuantity() == null && request.getPrice() == null)) {
            throw new IllegalArgumentException("At least one of quantity or price must be provided");
        }

        if (bookRepository.findById(id).isEmpty()) {
            throw new BookNotFoundException("Book not found: " + id);
        }

        if (request.getQuantity() != null) {
            bookRepository.updateQuantity(id, request.getQuantity());
        }

        if (request.getPrice() != null) {
            bookRepository.updatePrice(id, request.getPrice());
        }

        return findById(id);
    }

    private BookInfoResult toInfoResult(Book book) {
        return new BookInfoResult(book.getTitle(), book.getQuantity(), book.getPrice());
    }
}
