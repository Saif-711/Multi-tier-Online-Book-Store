package dos.project.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderRecord {
    private int id;
    private int bookId;
    private String title;
    private String purchasedAt;
}
