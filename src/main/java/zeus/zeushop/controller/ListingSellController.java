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
    private ListingSellService service;
    private final String listingSellRedirect;

    public ListingSellController(ListingSellService service) {
        this.service = service;
        this.listingSellRedirect = "redirect:/sell/list";
    }

    @GetMapping("/create")
    public String createListingSellPage(Model model) {
        ListingSell listingSell = new ListingSell();
        model.addAttribute("list", listingSell);
        return "createListingSell";
    }

    @PostMapping("/create")
    public String createListingSellPost(@ModelAttribute ListingSell listingSell, Model model) {
        service.create(listingSell);
        return listingSellRedirect;
    }

    @GetMapping("/list")
    public String listingSellPage(Model model) {
        List<ListingSell> allListingSell = service.findAll();
        model.addAttribute ("lists", allListingSell);
        return "sell";
    }

    @PostMapping("/delete/{id}")
    public String deleteListingSell(@PathVariable String id) {
        service.deleteListingSell(id);
        return listingSellRedirect;
    }

    @GetMapping("/edit/{id}")
    public String editListingSellPage(@PathVariable String id, Model model) {
        ListingSell listingSell = service.findById(id);
        model.addAttribute("listingSell", listingSell);
        return "editListingSell";
    }

    @PostMapping("/edit/{id}")
    public String editListingSellPost(@PathVariable String id, @ModelAttribute ListingSell listingSell) {
        listingSell.setId(id);
        service.editListingSell(listingSell);
        return listingSellRedirect;
    }
}