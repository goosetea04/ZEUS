package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zeus.zeushop.model.*;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.ListingService;
import zeus.zeushop.service.UserService;
import java.time.LocalDateTime;
import zeus.zeushop.service.ShoppingCartServiceFactory;
import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

@Controller
public class ListingController {

    @Autowired
    private UserService userService;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);
        List<Listing> allListings = listingService.getAllListings();
        model.addAttribute("listings", allListings);
        model.addAttribute("cartItem", new CartItem()); // For adding listings to cart
        return "listings";
    }


    @PostMapping("/add-to-cart")
    public String addToCart(@ModelAttribute("cartItem") CartItem cartItem,
                            @RequestParam("listingId") Integer listingId,
                            Model model) {
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

        // Retrieve currently authenticated user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        // Set the buyerId to the ID of the currently authenticated user

        Listing storedListing = listingRepository.findById(listingId).orElse(null);
        if (storedListing != null && storedListing.getProduct_quantity() < quantity) {
            model.addAttribute("error", "Not enough stock available.");
            return "redirect:/listings";
        }

        // Decrease the quantity of the listing
        // storedListing.setProduct_quantity(storedListing.getProduct_quantity() - quantity);
        // listingRepository.save(storedListing);

        // Add the listing to the cart
        shoppingCartService.addListingToCart(listing, quantity, currentUser.getId());

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

        // Retrieve currently authenticated user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        // Set the seller_id to the ID of the currently authenticated user
        listing.setSellerId(currentUser.getId());

        // Set the visible column to true
        listing.setVisible(true);

        // Create the listing
        listingService.createListing(listing);

        return "redirect:/manage-listings";
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
            return "redirect:/update-listings";
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
        // Retrieve currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        List<Listing> userListings = listingService.getListingsBySellerId(currentUser.getId());

        // Pass the filtered listings to the view for rendering
        model.addAttribute("listings", userListings);
        return "manage-listings";
    }

    @PostMapping("/delete-listing")
    public String deleteListing(@RequestParam("id") Long id) {
        listingService.deleteListing(id);
        return "redirect:/manage-listings"; // Redirect to the manage-listings page after deletion
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") Integer id, Model model) {
        Optional<Listing> listingOptional = listingService.getListingById(id.longValue());
        if (listingOptional.isPresent()) {
            model.addAttribute("listing", listingOptional.get());
            model.addAttribute("cartItem", new CartItem());
            return "product";
        } else {
            return "redirect:/listings"; // Handle the case where the listing is not found
        }
    }

}
