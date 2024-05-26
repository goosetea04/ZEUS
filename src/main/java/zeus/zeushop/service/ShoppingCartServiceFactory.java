package zeus.zeushop.service;

import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;

public class ShoppingCartServiceFactory {
    public static ShoppingCartService createShoppingCartService(CartItemRepository cartItemRepository, ListingRepository listingRepository) {
        if (cartItemRepository == null && listingRepository == null) {
            throw new IllegalArgumentException("CartItemRepository and ListingRepository cannot be null");
        }
        if (cartItemRepository == null) {
            throw new IllegalArgumentException("CartItemRepository cannot be null");
        }
        if (listingRepository == null) {
            throw new IllegalArgumentException("ListingRepository cannot be null");
        }
        return new ShoppingCartServiceImpl(cartItemRepository, listingRepository);
    }
}
