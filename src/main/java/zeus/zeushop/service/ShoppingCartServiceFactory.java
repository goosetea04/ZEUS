package zeus.zeushop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component; // Annotate with @Component
import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;

public class ShoppingCartServiceFactory {
    public static ShoppingCartService createShoppingCartService(CartItemRepository cartItemRepository, ListingRepository listingRepository) {
        return new ShoppingCartServiceImpl(cartItemRepository, listingRepository);
    }
}