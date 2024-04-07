package zeus.zeushop.model;

public class CartItem {
    private Listing listing; // Represents the listing associated with this cart item
    private int quantity;    // Represents the quantity of the listing added to the cart

    // Constructor
    public CartItem() {
        // Initialize with default values or leave them null/0
        this.listing = null;
        this.quantity = 0;
    }
    public CartItem(Listing listing, int quantity) {
        this.listing = listing;
        this.quantity = quantity;
    }

    // Getter and setter methods
    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Other methods as needed
}
