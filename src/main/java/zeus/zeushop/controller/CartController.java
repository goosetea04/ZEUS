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

        // Fetching the latest payment status
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
            // Convert cartItemId from Long to Integer
            Integer itemId = cartItemId.intValue();

            // Delete the cart item from the database
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

        // Create a new order
        Order order = new Order();
        order.setUser(currentUser);
        order.setStatus("CREATED");

        // Assuming all items in the cart are from the same seller for simplicity
        Integer sellerId = cartItems.get(0).getListing().getSellerId();
        User seller = userService.getUserById(sellerId); // Fetch the User entity based on sellerId
        order.setSeller(seller);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setListing(cartItem.getListing());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getListing().getProduct_price());
            order.getItems().add(orderItem); // Ensure items are added to the initialized list
        }

        orderService.createOrder(order);
        shoppingCartService.clearCartItemsByBuyerId(currentUser.getId());

        redirectAttributes.addFlashAttribute("success", "Order created successfully.");
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        List<Order> orders = orderService.getOrdersByUserId(currentUser.getId());
        model.addAttribute("orders", orders);

        return "orders";
    }
}

