package zeus.zeushop.controller;

import zeus.zeushop.model.Listing;
import zeus.zeushop.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
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

@RestController
@RequestMapping("/listings")
public class ListingController {
    @Autowired
    private ListingService listingService;
  
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ListingRepository listingRepository;

    @PostMapping
    public Listing createListing(@RequestBody Listing listing) {
        return listingService.createListing(listing);
    }

    @GetMapping
    public List<Listing> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    public Optional<Listing> getListingById(@PathVariable String id) {
        return listingService.getListingById(id);
    }

    @PutMapping("/{id}")
    public Listing updateListing(@PathVariable String id, @RequestBody Listing listingDetails) {
        return listingService.updateListing(id, listingDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable String id) {
        listingService.deleteListing(id);
    }

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
}
