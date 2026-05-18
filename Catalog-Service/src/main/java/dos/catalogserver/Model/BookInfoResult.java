package dos.catalogserver.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookInfoResult {
    private String title;
    private int quantity;
    private double price;
}
