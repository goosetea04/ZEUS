package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.Listing;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.repository.ListingRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final List<CartItem> cartItems;
    private final ListingRepository listingRepository;

    @Autowired
    public ShoppingCartServiceImpl(ListingRepository listingRepository) {
        this.cartItems = new ArrayList<>();
        this.listingRepository = listingRepository;
    }

    @Override
    public void addListingToCart(Listing listing, int quantity) {
        // Check if the item already exists in the cart
        for (CartItem item : cartItems) {
            if (item.getListing().equals(listing)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // If not, add a new CartItem to the cart
        cartItems.add(new CartItem(listing, quantity));
    }

    @Override
    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }
    // Implement other methods for managing the shopping cart
}
