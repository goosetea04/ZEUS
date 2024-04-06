package zeus.zeushop.service;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.model.Listing;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<CartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Listing listing, int quantity) {
        // Check if the item already exists in the cart
        for (CartItem item : items) {
            if (item.getListing().equals(listing)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // If not, add a new CartItem to the cart
        items.add(new CartItem(listing, quantity));
    }

    // Implement methods for removing items, updating quantities, etc.

    public List<CartItem> getItems() {
        return items;
    }
}
