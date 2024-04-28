package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter @Setter
public class ListingSell {
    private String id;
    private String name;
    private String description;
    private int stock;
    private int price;

    public ListingSell() {
        id = UUID.randomUUID().toString();
    }
}