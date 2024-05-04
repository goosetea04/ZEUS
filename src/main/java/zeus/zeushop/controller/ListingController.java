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
import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;
import org.springframework.web.bind.annotation.RequestParam;



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

    @Autowired
    private CartItemRepository cartItemRepository;

    @GetMapping("/listings")
    public String getAllListings(Model model) {
        ShoppingCartService shoppingCartService = ShoppingCartServiceFactory.createShoppingCartService(cartItemRepository, listingRepository);
        model.addAttribute("listings", shoppingCartService.getAllListings());
        model.addAttribute("cartItem", new CartItem()); // For adding listings to cart
        return "listings";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@ModelAttribute("cartItem") CartItem cartItem, @RequestParam("listingId") Integer listingId, Model model) {
        Listing listing = listingRepository.findById(listingId).orElse(null);
        if (listing == null) {
            // Handle case where listing is not found
            return "redirect:/listings";
        }

        int quantity = cartItem.getQuantity();
        if (quantity <= 0) {
            model.addAttribute("error", "Quantity must be a positive number.");
            return "redirect:/listings";
        }

        Listing storedListing = listingRepository.findById(listingId).orElse(null);
        if (storedListing != null && storedListing.getProduct_quantity() < quantity) {
            model.addAttribute("error", "Not enough stock available.");
            return "redirect:/listings";
        }

        // Decrease the quantity of the listing
        storedListing.setProduct_quantity(storedListing.getProduct_quantity() - quantity);
        listingRepository.save(storedListing);

        // Add the listing to the cart
        shoppingCartService.addListingToCart(listing, quantity);

        return "redirect:/listings";
    }

    @GetMapping("/add-listing")
    public String showAddListingForm(Model model) {
        model.addAttribute("listing", new Listing());
        return "add-listing";
    }
    @PostMapping("/save-listing")
    public String saveListing(@ModelAttribute Listing listing, Model model) {
        if (listing.getProduct_quantity() < 0 || listing.getProduct_price() < 0) {
            model.addAttribute("error", "Stock and price cannot be negative.");
            return "add-listing";
        }
        listingService.createListing(listing);
        return "redirect:/listings";
    }
    @GetMapping("/listings-cart")
    public String showCart() {
        return "cart";
    }
}
