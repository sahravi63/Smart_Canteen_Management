package com.example.canteen.web;

import com.example.canteen.service.MenuItemService;
import com.example.canteen.service.UserService;
import com.example.canteen.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MenuItemService menuService;
    private final UserService userService;
    private final OrderService orderService;

    public AdminController(MenuItemService menuService, UserService userService, OrderService orderService) {
        this.menuService = menuService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("menuCount", menuService.findAll().size());
        model.addAttribute("orderCount", orderService.findAll().size());
        model.addAttribute("recentOrders", orderService.findRecentOrders()); // optional
        return "admin/dashboard";
    }

}
