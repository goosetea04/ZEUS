package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import zeus.zeushop.model.OrderList;
import zeus.zeushop.service.OrderListService;
import zeus.zeushop.model.User;
import zeus.zeushop.service.UserService;

import java.util.List;

@Controller
public class OrderListController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderListService orderListService;

    @GetMapping("/order-lists")
    public String getAllOrderList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);
        List<OrderList> orders = orderListService.findOrdersBySellerAndStatus(currentUser.getId(), "APPROVED");
        model.addAttribute("orders", orders);
        return "order-list";  // Make sure this matches your Thymeleaf template name
    }
}

