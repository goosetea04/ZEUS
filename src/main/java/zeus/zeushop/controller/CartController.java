package zeus.zeushop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.CartItem;
import zeus.zeushop.model.Payment;
import zeus.zeushop.model.User;
import zeus.zeushop.service.PaymentService;
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

    @Autowired
    private PaymentService paymentService;
    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        List<CartItem> cartItems = shoppingCartService.getCartItemsByBuyerId(currentUser.getId());
        BigDecimal totalCost = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getListing().getProduct_price())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("balance", currentUser.getBalance());

        // Fetching the latest payment status
        String paymentStatus = paymentService.getLatestPaymentStatus(currentUser.getId());
        model.addAttribute("paymentStatus", paymentStatus);

        return "cart";
    }

    private boolean allItemsApproved(List<CartItem> cartItems) {
        return cartItems.stream().noneMatch(item -> "PENDING".equals(item.getStatus()));
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

