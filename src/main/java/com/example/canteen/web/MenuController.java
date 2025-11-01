package com.example.canteen.web;

import com.example.canteen.model.MenuItem;
import com.example.canteen.service.MenuItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/menu")
public class MenuController {

    private final MenuItemService menuService;

    public MenuController(MenuItemService menuService) {
        this.menuService = menuService;
    }

    // User menu view
    @GetMapping
    public String listMenu(Model model) {
        List<MenuItem> menuItems = menuService.findAll();
        model.addAttribute("menuItems", menuItems); // ✅ match the template
        return "user/menu";
    }

    // Vendor endpoints
    @GetMapping("/vendor")
    public String vendorMenu(Model model) {
        model.addAttribute("items", menuService.findAll());
        return "vendor/menu";
    }

    @GetMapping("/vendor/new")
    public String newItemForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        return "vendor/menu-form";
    }

    @PostMapping("/vendor/save")
    public String saveMenuItem(@ModelAttribute MenuItem menuItem) {
        menuService.save(menuItem);
        return "redirect:/user/menu/vendor";
    }


}
