package dos.project.Repository;

import dos.project.Model.OrderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;

@Repository
public class OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public OrderRecord save(int bookId, String title) {
        String purchasedAt = Instant.now().toString();
        String sql = "INSERT INTO orders (book_id, title, purchased_at) VALUES (?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bookId);
            ps.setString(2, title);
            ps.setString(3, purchasedAt);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        int id = key != null ? key.intValue() : 0;
        return new OrderRecord(id, bookId, title, purchasedAt);
    }
}
