package zeus.zeushop.service;

import zeus.zeushop.model.CartItem;
import zeus.zeushop.model.Listing;

import java.util.List;

public interface ShoppingCartService {
    void addListingToCart(Listing listing, int quantity);
    void addListingToCart(Listing listing, int quantity, int buyerID);
    List<Listing> getAllListings();
    List<CartItem> getAllCartItems();
    List<CartItem> getCartItemsByBuyerId(Integer buyerId);
    void markItemsPending(List<CartItem> cartItems);
    void clearCartItemsByBuyerId(Integer buyerId);
}
