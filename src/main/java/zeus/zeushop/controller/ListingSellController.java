package zeus.zeushop.controller;

import zeus.zeushop.model.ListingSell;
import zeus.zeushop.service.ListingSellService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/sell")
public class ListingSellController {
    @Autowired
    private ListingSellService service;

    @GetMapping("/create")
    public String createListingSellPage(Model model) {
        ListingSell listingSell = new ListingSell();
        model.addAttribute("name", listingSell);
        return "createListingSell";
    }

    @PostMapping("/create")
    public String createListingSellPost(@ModelAttribute ListingSell listingSell, Model model) {
        service.create(listingSell);
        return "redirect:list";
    }

    @GetMapping("/list")
    public String listingSellPage(Model model) {
        List<ListingSell> allListingSell = service.findAll();
        model.addAttribute ("name", allListingSell);
        return "listingSell";
    }

    @GetMapping("/delete/{id}")
    public String deleteListingSell(@PathVariable String id) {
        ListingSell listing = service.findById(id);
        service.deleteListingSell(listing);
        return "redirect:/product/list";
    }

    @GetMapping("/edit/{id}")
    public String editPage(Model model, @PathVariable String id) {
        ListingSell listing = service.findById(id);
        model.addAttribute("product", listing);
        return "editListingSell";
    }

    @PostMapping("/edit")
    public String editListingSell(@ModelAttribute ListingSell listingSell) {
        service.editListingSell(listingSell);
        return "redirect:list";
    }
}