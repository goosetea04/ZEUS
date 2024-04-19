package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.ShoppingCartServiceFactory;
import zeus.zeushop.repository.ListingRepository;
import zeus.zeushop.model.Listing;

@Controller
public class ListingController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ListingRepository listingRepository;

    @GetMapping("/listings")
    public String getAllListings(Model model) {
        ShoppingCartService shoppingCartService = ShoppingCartServiceFactory.createShoppingCartService(listingRepository);
        model.addAttribute("listings", shoppingCartService.getAllListings());
        model.addAttribute("cartItem", new CartItem()); // For adding listings to cart
        return "listings";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@ModelAttribute("cartItem") CartItem cartItem) {
        ShoppingCartService shoppingCartService = ShoppingCartServiceFactory.createShoppingCartService(listingRepository);
        shoppingCartService.addListingToCart(cartItem.getListing(), cartItem.getQuantity());
        return "redirect:/listings";
    }

    @GetMapping("/add-listing")
    public String showAddListingForm() {
        return "add-listing";
    }
    @PostMapping("/save-listing")
    public String saveListing() {
        // listingRepository.save(listing);
        // Assuming there is logic to save the listing to the repository
        return "redirect:/listings";
    }
}
