package com.example.canteen.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayClient client;

    public RazorpayService(
            @Value("${razorpay.key}") String key,
            @Value("${razorpay.secret}") String secret
    ) throws RazorpayException {
        this.client = new RazorpayClient(key, secret);
    }

    public String createOrder(int amountInPaise, String currency, String receipt) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);
        orderRequest.put("payment_capture", 1);

        Order order = client.orders.create(orderRequest);
        return order.get("id"); // ✅ Return only Order ID (String)
    }
}
