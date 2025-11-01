package com.example.canteen.web;

import com.example.canteen.model.*;
import com.example.canteen.security.CustomUserDetails;
import com.example.canteen.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/orders")
public class OrderController {
    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public String listOrders(@AuthenticationPrincipal CustomUserDetails cud, Model model) {
        List<Order> orders = orderService.findByUser(cud.getId());
        model.addAttribute("orders", orders);
        return "user/orders";
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal CustomUserDetails cud) {
        User user = userService.findById(cud.getId()).orElseThrow();
        List<CartItem> cartItems = cartService.listForUser(user.getId());
        if (cartItems.isEmpty()) return "redirect:/cart?empty";
        orderService.placeOrderFromCart(user, cartItems);
        return "redirect:/user/orders";
    }
}
