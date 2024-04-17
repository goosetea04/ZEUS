package zeus.zeushop.model;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class Listing {
    private String listingID;
    private String listingName;
    private String listingDescription;
    private String SellerID;
    private int listingStock;

    private int listingPrice;
}
