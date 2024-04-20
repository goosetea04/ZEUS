package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import zeus.zeushop.model.*;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.ListingService;
import java.time.LocalDateTime;
import zeus.zeushop.service.ShoppingCartServiceFactory;
import zeus.zeushop.repository.ListingRepository;


@Controller
public class ListingController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    private final ListingService listingService;
    @Autowired
    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }
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
    public String showAddListingForm(Model model) {
        model.addAttribute("listing", new Listing());
        return "add-listing";
    }
    @PostMapping("/save-listing")
    public String saveListing(@ModelAttribute Listing listing) {
        listingService.createListing(listing);
        return "redirect:/listings";
    }
    @GetMapping("/cart")
    public String showCart() {
        return "cart";
    }
}
