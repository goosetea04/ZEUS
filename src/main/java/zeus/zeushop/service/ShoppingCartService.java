package zeus.zeushop.service;

import zeus.zeushop.model.Listing;

import java.util.List;

public interface ShoppingCartService {
    void addListingToCart(Listing listing, int quantity);
    List<Listing> getAllListings();
    // Add other methods for managing the shopping cart
}
