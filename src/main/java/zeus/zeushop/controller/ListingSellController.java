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
    private final String listingSellRedirect;

    public ListingSellController(ListingSellService service) {
        this.service = service;
        this.listingSellRedirect = "redirect:/sell/list";
    }

    @GetMapping("/create")
    public String createListingSellPage(Model model) {
        ListingSell listingSell = new ListingSell();
        model.addAttribute("listing", listingSell);
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
        model.addAttribute ("listing", allListingSell);
        return "Lists";
    }

    @GetMapping("/delete/{id}")
    public String deleteListingSell(@PathVariable String id) {
        service.deleteListingSell(id);
        return listingSellRedirect;
    }

    @GetMapping("/edit/{id}")
    public String editGet(@PathVariable String id, Model model) {
        ListingSell listing = service.findById(id);
        model.addAttribute("listing", listing);
        return "ListingSellEdit";
    }

    @PostMapping("/edit")
    public String editPut(@ModelAttribute ListingSell listingSell) {
        service.editListingSell(listingSell);
        return listingSellRedirect;
    }
}