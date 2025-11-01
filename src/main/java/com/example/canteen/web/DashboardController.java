package com.example.canteen.web;

import com.example.canteen.model.User;
import com.example.canteen.service.AdminService;
import com.example.canteen.service.MenuItemService;
import com.example.canteen.service.OrderService;
import com.example.canteen.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserService userService;
    private final MenuItemService menuItemService;
    private final OrderService orderService;
    private final AdminService adminService;

    public DashboardController(UserService userService,
                               MenuItemService menuItemService,
                               OrderService orderService,
                               AdminService adminService) {
        this.userService = userService;
        this.menuItemService = menuItemService;
        this.orderService = orderService;
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");

        if (loggedUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedUser);

        switch (loggedUser.getRole()) {
            case ADMIN -> {
                model.addAttribute("totalUsers", adminService.countAllUsers());
                model.addAttribute("pendingVendors", adminService.countPendingVendors());
                model.addAttribute("totalOrders", orderService.countOrders());
                model.addAttribute("totalMenuItems", menuItemService.countItems());
                return "admin/dashboard";
            }
            case VENDOR -> {
                model.addAttribute("menuCount", menuItemService.countByVendor(loggedUser.getId()));
                model.addAttribute("orderCount", orderService.countOrdersByVendor(loggedUser.getId()));
                return "vendor/dashboard";
            }
            case STUDENT -> {
                model.addAttribute("orderCount", orderService.countOrdersByUser(loggedUser.getId()));
                model.addAttribute("walletBalance", loggedUser.getWalletBalance());
                return "user/dashboard";
            }
            default -> {
                return "redirect:/login";
            }
        }
    }
}
