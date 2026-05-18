package dos.catalogserver.Repository;

import dos.catalogserver.Model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Book> findByTopic(String topic) {
        String sql = "SELECT * FROM books WHERE topic = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Book.class), topic);
    }

    public Optional<Book> findById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try {
            Book book = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Book.class), id);
            return Optional.ofNullable(book);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public void updateQuantity(int id, int quantity) {
        String sql = "UPDATE books SET quantity = ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, id);
    }

    public void updatePrice(int id, double price) {
        String sql = "UPDATE books SET price = ? WHERE id = ?";
        jdbcTemplate.update(sql, price, id);
    }

    public int decrementQuantity(int id) {
        String sql = "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";
        return jdbcTemplate.update(sql, id);
    }

    public int applyQuantityDelta(int id, int delta) {
        if (delta == -1) {
            return decrementQuantity(id);
        }
        String sql = "UPDATE books SET quantity = quantity + ? WHERE id = ? AND quantity + ? >= 0";
        return jdbcTemplate.update(sql, delta, id, delta);
    }
}
