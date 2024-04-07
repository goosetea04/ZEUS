package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.service.ShoppingCartService;

@Controller
public class ListingController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/listings")
    public String getAllListings(Model model) {
        // Assuming you have a method in your service to retrieve all listings
        model.addAttribute("listings", shoppingCartService.getAllListings());
        model.addAttribute("cartItem", new CartItem()); // For adding listings to cart
        return "listings";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@ModelAttribute("cartItem") CartItem cartItem) {
        // Assuming you have a method in your service to add listings to the cart
        shoppingCartService.addListingToCart(cartItem.getListing(), cartItem.getQuantity());
        return "redirect:/listings";
    }
}
