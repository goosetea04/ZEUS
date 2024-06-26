package zeus.zeushop.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.*;
import zeus.zeushop.service.*;
import zeus.zeushop.repository.CartItemRepository;

import zeus.zeushop.model.CartItem;

import zeus.zeushop.model.User;
import zeus.zeushop.service.PaymentService;
import zeus.zeushop.service.ShoppingCartService;
import zeus.zeushop.repository.CartItemRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@Controller
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderService orderService;

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


        String paymentStatus = paymentService.getLatestPaymentStatus(currentUser.getId());
        model.addAttribute("paymentStatus", paymentStatus);

        return "cart";
    }

    private boolean allItemsApproved(List<CartItem> cartItems) {
        return cartItems.stream().noneMatch(item -> "PENDING".equals(item.getStatus()));
    }

    @PostMapping("/remove-from-cart")
    @ResponseBody
    public ResponseEntity<String> removeFromCart(@RequestParam("cartItemId") Long cartItemId) {
        try {
            Integer itemId = cartItemId.intValue();
            cartItemRepository.deleteById(itemId);

            // Return success response
            return ResponseEntity.ok("Item removed from cart successfully.");
        } catch (Exception e) {
            // Return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing item from cart: " + e.getMessage());
        }
    }

    @PostMapping("/create-order")
    public String createOrder(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        List<CartItem> cartItems = shoppingCartService.getCartItemsByBuyerId(currentUser.getId());

        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        BigDecimal totalCost = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getListing().getProduct_price())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(currentUser);
        order.setTotalCost(totalCost);
        order.setStatus("CREATED");

        order = orderService.createOrder(order);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setListing(cartItem.getListing());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderService.createOrder(order);
        }

        shoppingCartService.clearCartItemsByBuyerId(currentUser.getId());

        redirectAttributes.addFlashAttribute("success", "Order created successfully.");
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        List<Order> orders = orderService.getOrdersByUserId(Long.valueOf(currentUser.getId()));
        model.addAttribute("orders", orders);

        return "orders";
    }
}

