package zeus.zeushop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zeus.zeushop.model.ListingSell;
import zeus.zeushop.model.User;
import zeus.zeushop.service.ListingSellService;
import zeus.zeushop.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
public class ListingSellController {
    private final ListingSellService service;
    private final UserService userService;
    private static final String REDIRECT_LIST = "redirect:/sell-list";

    @Autowired
    public ListingSellController(ListingSellService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createListingSellPage(Model model) {
        model.addAttribute("listingSell", new ListingSell());
        return "createListingSell";
    }

    @PostMapping("/create")
    public String createListingSellPost(@ModelAttribute ListingSell listingSell, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);
        listingSell.setSellerId(currentUser.getId());

        service.create(listingSell);
        redirectAttributes.addFlashAttribute("successMessage", "Listing created successfully.");
        return REDIRECT_LIST;
    }

    @GetMapping("/sell-list")
    public String listingSellPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.getUserByUsername(currentUsername);

        List<ListingSell> userListingSells = service.findBySellerId(currentUser.getId());
        model.addAttribute("listingSells", userListingSells);
        return "sell";
    }

    @PostMapping("/delete/{id}")
    public String deleteListingSell(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        service.deleteListingSell(id);
        redirectAttributes.addFlashAttribute("successMessage", "Listing deleted successfully.");
        return REDIRECT_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editListingSellPage(@PathVariable Integer id, Model model) {
        Optional<ListingSell> optionalListingSell = service.findById(id);
        if (optionalListingSell.isPresent()) {
            model.addAttribute("listingSell", optionalListingSell.get());
            return "editListingSell";
        } else {
            return REDIRECT_LIST;
        }
    }

    @PostMapping("/edit/{id}")
    public String editListingSellPost(@PathVariable Integer id, @ModelAttribute ListingSell listingSell, RedirectAttributes redirectAttributes) {
        listingSell.setProduct_id(id);
        service.editListingSell(id, listingSell);
        redirectAttributes.addFlashAttribute("successMessage", "Listing updated successfully.");
        return REDIRECT_LIST;
    }
}