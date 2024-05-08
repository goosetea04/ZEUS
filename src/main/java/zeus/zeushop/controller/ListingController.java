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
import java.util.stream.Collectors;

import java.util.*;

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
        List<Listing> visibleListings = listingService.getAllListings().stream()
                .filter(Listing::isVisible)
                .collect(Collectors.toList());
        model.addAttribute("listings", visibleListings);
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

        // Set the visible column to true
        listing.setVisible(true);

        // Create the listing
        listingService.createListing(listing);

        return "redirect:/listings";
    }

    @GetMapping("/listings-cart")
    public String showCart() {
        return "cart";
    }

    @GetMapping("/update-listing")
    public String showUpdateListingForm(@RequestParam("id") Integer id, Model model) {
        Listing listing = listingService.getListingById(Long.valueOf(id)).orElse(null);
        if (listing == null) {
            // Handle case where listing is not found
            return "redirect:/listings";
        }
        model.addAttribute("listing", listing);
        return "update-listing";
    }


    @PostMapping("/update-listing")
    public String updateListing(@ModelAttribute Listing updatedListing, @RequestParam("id") Long id, Model model) {
        // Get the original listing from the database
        Listing originalListing = listingService.getListingById(id).orElse(null);
        if (originalListing == null) {
            // Handle case where original listing is not found
            return "redirect:/listings";
        }

        // Update the fields of the original listing with the new values
        originalListing.setProduct_name(updatedListing.getProduct_name());
        originalListing.setProduct_quantity(updatedListing.getProduct_quantity());
        originalListing.setProduct_description(updatedListing.getProduct_description());
        originalListing.setProduct_price(updatedListing.getProduct_price());

        System.out.println(updatedListing.getProduct_description());
        System.out.println(updatedListing.getProduct_price());

        // Save the updated listing
        listingService.updateListing(id, originalListing);

        return "redirect:/listings";
    }

    @GetMapping("/manage-listings")
    public String manageListings(Model model) {
        List<Listing> allListings = listingService.getAllListings();
        model.addAttribute("listings", allListings);
        return "manage-listings";
    }

    @PostMapping("/delete-listing")
    public String deleteListing(@RequestParam("id") Long id) {
        listingService.deleteListing(id);
        return "redirect:/manage-listings"; // Redirect to the manage-listings page after deletion
    }
}
