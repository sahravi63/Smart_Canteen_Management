package com.example.canteen.web;

import com.example.canteen.model.CartItem;
import com.example.canteen.security.CustomUserDetails;
import com.example.canteen.service.CartService;
import com.example.canteen.service.RazorpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RazorpayService razorpayService;

    @GetMapping("/user/checkout")
    public String checkoutPage(@AuthenticationPrincipal CustomUserDetails user, Model model) {

        if (user == null) {
            return "redirect:/login";
        }

        Long userId = user.getId();

        // ✅ Fetch cart items
        List<CartItem> cartItems = cartService.listForUser(userId);

        // ✅ Calculate total
        double totalAmount = cartItems.stream()
                .map(CartItem::getSubtotal)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(java.math.BigDecimal::doubleValue)
                .sum();

        int itemCount = cartItems.size();
        int amountInPaise = (int) (totalAmount * 100);

        String orderId = null;
        try {
            orderId = razorpayService.createOrder(amountInPaise, "INR", "order_" + userId);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Payment initialization failed. Try again.");
            return "user/checkout";
        }


        // ✅ Send data to view
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", totalAmount);
        model.addAttribute("itemCount", itemCount);
        model.addAttribute("amount", amountInPaise);
        model.addAttribute("currency", "INR");
        model.addAttribute("razorpayOrderId", orderId);
        model.addAttribute("razorpayKey", "rzp_test_ABC123"); // TODO: replace with actual key

        return "user/checkout";
    }

}
