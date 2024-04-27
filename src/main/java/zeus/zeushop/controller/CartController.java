package zeus.zeushop.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import zeus.zeushop.model.*;
import zeus.zeushop.service.ShoppingCartService;
import java.util.List;


@Controller
public class CartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/cart")
    public String showCart(Model model) {
        List<CartItem> cartItems = shoppingCartService.getAllCartItems();
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }
}

