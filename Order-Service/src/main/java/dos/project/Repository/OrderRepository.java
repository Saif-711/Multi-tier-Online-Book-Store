package dos.project.Repository;

import dos.project.Model.OrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public OrderRecord save(int bookId, String title) {
        String purchasedAt = Instant.now().toString();

        // Insert the order row
        String insertSql = "INSERT INTO orders (book_id, title, purchased_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(insertSql, bookId, title, purchasedAt);

        // SQLite-safe way to get the last inserted ID — GeneratedKeyHolder
        // does NOT work reliably with the SQLite JDBC driver.
        String idSql = "SELECT last_insert_rowid()";
        Integer id = jdbcTemplate.queryForObject(idSql, Integer.class);

        return new OrderRecord(id != null ? id : 0, bookId, title, purchasedAt);
    }
}
