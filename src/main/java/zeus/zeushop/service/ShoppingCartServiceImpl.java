package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.Listing;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.repository.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ListingRepository listingRepository;


    @Autowired
    public ShoppingCartServiceImpl(CartItemRepository cartItemRepository, ListingRepository listingRepository) {
        this.cartItemRepository = cartItemRepository;
        this.listingRepository = listingRepository;
    }

    @Override
    public void addListingToCart(Listing listing, int quantity) {
        if (listing != null) {
            CartItem cartItem = new CartItem();
            cartItem.setListing(listing);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            throw new IllegalArgumentException("Listing cannot be null");
        }
    }

    @Override
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }
    @Override
    public List<CartItem> getAllCartItems() {
        // Implement logic to retrieve all cart items from the repository
        // This might involve fetching them from cartItemRepository
        List<CartItem> cartItems = cartItemRepository.findAll();
        return cartItems;
    }

}
