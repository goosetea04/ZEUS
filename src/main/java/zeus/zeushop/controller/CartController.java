package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.model.Listing;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CartItemRepository cartItemRepository;
    @GetMapping("/cart")
    public String showCart(Model model) {
        List<CartItem> cartItems = shoppingCartService.getAllCartItems();
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @PostMapping("/remove-from-cart")
    public String removeFromCart(@RequestParam("cartItemId") Long cartItemId) {
        // Convert cartItemId from Long to Integer
        Integer itemId = cartItemId.intValue();

        // Delete the cart item from the database
        cartItemRepository.deleteById(itemId);

        return "redirect:/cart";
    }

}

