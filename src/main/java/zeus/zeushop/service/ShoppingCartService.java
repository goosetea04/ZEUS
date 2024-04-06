package zeus.zeushop.service;
import org.springframework.stereotype.Service;
import zeus.zeushop.model.Listing;
import zeus.zeushop.repository.ListingRepository;
import java.util.*;

@Service
public class ShoppingCartService {
    private ListingRepository listingRepository;
    private ShoppingCart shoppingCart;

    public ShoppingCartService() {
        this.shoppingCart = new ShoppingCart();
    }

    public void addListingToCart(Listing listing, int quantity) {
        shoppingCart.addItem(listing, quantity);
    }
    public List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<>();
        Iterator<Listing> iterator = listingRepository.findAll();
        while (iterator.hasNext()) {
            listings.add(iterator.next());
        }
        return listings;
    }

    // Other methods for managing the shopping cart
}
