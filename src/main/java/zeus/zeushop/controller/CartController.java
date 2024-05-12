package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.model.User;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.repository.CartItemRepository;
import zeus.zeushop.repository.ListingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import zeus.zeushop.service.UserService;

@Controller
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CartItemRepository cartItemRepository;
    @GetMapping("/cart")
    public String showCart(Model model) {
        // Retrieve currently authenticated user's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        // Get cart items associated with the current user's ID
        List<CartItem> cartItems = shoppingCartService.getCartItemsByBuyerId(currentUser.getId());

        // Pass the cart items to the view
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("balance", currentUser.getBalance());
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

    @PostMapping("/pay")
    public String processPayment(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);
        List<CartItem> cartItems = shoppingCartService.getCartItemsByBuyerId(currentUser.getId());

        BigDecimal totalCost = BigDecimal.valueOf(cartItems.stream()
                .mapToDouble(item -> item.getListing().getProduct_price() * item.getQuantity())
                .sum());

        if (currentUser.getBalance().compareTo(totalCost) >= 0) {
            BigDecimal newBalance = currentUser.getBalance().subtract(totalCost);
            userService.updateUserBalance(currentUser.getId(), newBalance); // Update balance
            cartItemRepository.deleteAllInBatch(cartItems); // Clear the cart after payment
            redirectAttributes.addFlashAttribute("success", "Payment successful!");
            return "redirect:/cart";
        } else {
            redirectAttributes.addFlashAttribute("error", "Insufficient balance. Please top up your account.");
            return "redirect:/cart";
        }
    }

}

