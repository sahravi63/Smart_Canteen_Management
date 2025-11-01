package com.example.canteen.web;

import com.example.canteen.service.RazorpayService;
import jakarta.servlet.http.HttpSession;
import com.example.canteen.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @GetMapping("/checkout")
    public String checkoutPage(Model model, HttpSession session) {

        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser == null) {
            return "redirect:/login";
        }

        try {
            int amountInPaise = 50000; // ₹500
            String receipt = "order_" + loggedUser.getId();

            // ✅ Create Razorpay Order (returns String orderId)
            String orderId = razorpayService.createOrder(amountInPaise, "INR", receipt);

            model.addAttribute("razorpayOrderId", orderId);
            model.addAttribute("razorpayKey", "rzp_test_YourTestKeyHere"); // change to real key
            model.addAttribute("amount", amountInPaise);
            model.addAttribute("currency", "INR");

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        return "user/checkout";
    }

    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("payment_id") String paymentId,
            Model model
    ) {
        model.addAttribute("paymentId", paymentId);
        return "user/payment-success";
    }
}
