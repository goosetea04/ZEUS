package zeus.zeushop.service;

import zeus.zeushop.model.CartItem;
import zeus.zeushop.model.Listing;

import java.util.List;

public interface ShoppingCartService {
    void addListingToCart(Listing listing, int quantity);
    List<Listing> getAllListings();
    List<CartItem> getAllCartItems();
    // Add other methods for managing the shopping cart
}
