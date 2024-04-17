package zeus.zeushop.service;

import zeus.zeushop.repository.ListingRepository;

public class ShoppingCartServiceFactory {

    public static ShoppingCartService createShoppingCartService(ListingRepository listingRepository) {
        return new ShoppingCartServiceImpl(listingRepository);
    }
}
