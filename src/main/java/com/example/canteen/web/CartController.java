package com.example.canteen.web;

import com.example.canteen.model.CartItem;
import com.example.canteen.model.MenuItem;
import com.example.canteen.model.User;
import com.example.canteen.security.CustomUserDetails;
import com.example.canteen.service.CartService;
import com.example.canteen.service.MenuItemService;
import com.example.canteen.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/cart")
public class CartController {

    private final CartService cartService;
    private final MenuItemService menuService;
    private final UserService userService;

    public CartController(CartService cartService, MenuItemService menuService, UserService userService) {
        this.cartService = cartService;
        this.menuService = menuService;
        this.userService = userService;
    }

    /** Show cart page */
    @GetMapping
    public String viewCart(@AuthenticationPrincipal CustomUserDetails cud, Model model) {
        List<CartItem> items = cartService.listForUser(cud.getId());
        model.addAttribute("cartItems", items);
        double total = items.stream()
                .map(CartItem::getSubtotal)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(java.math.BigDecimal::doubleValue)
                .sum();
        model.addAttribute("cartTotal", total);
        return "user/cart";
    }

    /** Add an item to cart */
    @PostMapping("/add")
    public String addToCart(@AuthenticationPrincipal CustomUserDetails cud,
                            @RequestParam("itemId") Long itemId,
                            @RequestParam(name = "qty", defaultValue = "1") Integer qty) {

        System.out.println(">>> addToCart called for user=" + cud.getId() + " item=" + itemId + " qty=" + qty);

        MenuItem menuItem = menuService.findById(itemId).orElse(null);
        if (menuItem == null) {
            System.out.println("Menu item not found!");
            return "redirect:/user/menu?error";
        }

        User user = userService.findById(cud.getId()).orElseThrow();
        CartItem item = new CartItem();
        item.setUser(user);
        item.setMenuItem(menuItem);
        item.setQuantity(qty);

        CartItem saved = cartService.addItem(item);
        System.out.println("Saved cart item id=" + saved.getId());

        return "redirect:/user/cart";
    }

    /** Remove item from cart */
    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {
        cartService.remove(id);
        return "redirect:/user/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal CustomUserDetails cud) {
        cartService.clearUserCart(cud.getId());
        return "redirect:/user/cart";
    }
}
