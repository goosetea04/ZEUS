package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zeus.zeushop.model.*;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.service.ListingService;
import zeus.zeushop.service.UserService;

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
    public ListingController(ListingService listingService, ShoppingCartService shoppingCartService, ListingRepository listingRepository, CartItemRepository cartItemRepository, UserService userService) {
        this.listingService = listingService;
        this.listingRepository = listingRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.shoppingCartService = shoppingCartService;
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
        Boolean isAdmin = Objects.equals(currentUser.getRole(), "ADMIN");

        List<Listing> visibleListings = listingService.getAllListings().stream()
                .filter(listing -> listing.isVisible() && listing.getSellerId() != null && !listing.getSellerId().equals(currentUser.getId()))
                .collect(Collectors.toList());

        List<Listing> featuredListings = listingService.getAllFeatured().stream()
                .filter(listing -> listing.isVisible() && listing.getSellerId() != null && !listing.getSellerId().equals(currentUser.getId()))
                .collect(Collectors.toList());

        model.addAttribute("admin", isAdmin);
        model.addAttribute("listings", visibleListings);
        model.addAttribute("featured_listings", featuredListings);
        model.addAttribute("cartItem", new CartItem()); // For adding listings to cart
        return "listings";
    }


    @PostMapping("/add-to-cart")
    public String addToCart(@ModelAttribute("cartItem") CartItem cartItem,
                            @RequestParam("listingId") Integer listingId,
                            Model model) {
        Listing listing = listingRepository.findById(listingId).orElse(null);
        if (listing == null) {
            return "redirect:/listings";
        }

        int quantity = cartItem.getQuantity();
        if (quantity <= 0) {
            model.addAttribute("error", "Quantity must be a positive number.");
            return "redirect:/listings";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);


        Listing storedListing = listingRepository.findById(listingId).orElse(null);
        if (storedListing != null && storedListing.getProduct_quantity() < quantity) {
            model.addAttribute("error", "Not enough stock available.");
            return "redirect:/listings";
        }
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

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

    @GetMapping("/feature-listing")
    public String showFeatureListingForm(@RequestParam("id") Integer id, Model model) {
        Listing listing = listingService.getListingById(Long.valueOf(id)).orElse(null);
        if (listing == null) {
            return "redirect:/listings";
        }
        model.addAttribute("listing", listing);
        return "feature-listing";
    }

    @PostMapping("/feature-listing")
    public String featureListing(@ModelAttribute Listing featuredListing, @RequestParam("id") Long id, Model model) {
        Listing listing = listingService.getListingById(id).orElse(null);
        if (listing == null) {
            return "redirect:/listings";
        }
        listing.setEnd_date(featuredListing.getEnd_date());

        // Save the updated listing
        listingService.updateListing(id, listing);

        return "redirect:/listings";
    }

    @GetMapping("/delete-feature-listing")
    public String deleteFeatureListing(@ModelAttribute Listing featuredListing, @RequestParam("id") Long id, Model model) {
        Listing listing = listingService.getListingById(id).orElse(null);
        if (listing == null) {
            return "redirect:/listings";
        }
        listing.setEnd_date(LocalDateTime.now());

        // Save the updated listing
        listingService.updateListing(id, listing);

        return "redirect:/listings";
    }

    @PostMapping("/update-listing")
    public String updateListing(@ModelAttribute Listing updatedListing, @RequestParam("id") Long id, Model model) {
        // Get the original listing from the database
        Listing originalListing = listingService.getListingById(id).orElse(null);
        if (originalListing == null) {
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
        return "redirect:/manage-listings";
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
