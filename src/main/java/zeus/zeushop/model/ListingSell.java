package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ListingSell {
    private String ProductSellId;
    private String ProductSellName;
    private String ProductSellDescription;
    private int ProductSellStock;
    private int ProductSellPrice;
}