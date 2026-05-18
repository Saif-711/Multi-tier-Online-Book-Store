package dos.catalogserver.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {
    private int id;
    private String title;
    private String topic;
    private int quantity;
    private double price;
}
