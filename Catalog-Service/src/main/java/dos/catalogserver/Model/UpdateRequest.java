package dos.catalogserver.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequest {
    private Integer quantity;
    private Double price;
}
