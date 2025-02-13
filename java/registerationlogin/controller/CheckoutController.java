package registerationlogin.controller;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import registerationlogin.entity.Cart;
import registerationlogin.entity.CartItem;
import registerationlogin.entity.Feedback;
import registerationlogin.entity.Restaurant;
import registerationlogin.entity.Order;
import registerationlogin.entity.OrderItem;
import registerationlogin.entity.User;
import registerationlogin.repository.RestaurantRepository;
import registerationlogin.service.CartService;
import registerationlogin.service.FeedbackService;
import registerationlogin.service.OrderItemService;
import registerationlogin.service.CartItemService;
import registerationlogin.service.OrderService;
import registerationlogin.service.UserService;

@Controller
@RequestMapping("/")
public class CheckoutController {
    private CartService cartService;
    private CartItemService cartItemService;
    private RestaurantRepository restaurantRepository;
    private OrderService orderService; // Assuming you have a service to handle orders
    private OrderItemService orderItemService;
    private UserService userService;
    private FeedbackService feedbackService;

    public CheckoutController(CartService cartService, CartItemService cartItemService,
            RestaurantRepository restaurantRepository, OrderService orderService, OrderItemService orderItemService,
            UserService userService, FeedbackService feedbackService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.restaurantRepository = restaurantRepository;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.userService = userService;
        this.feedbackService = feedbackService;
    }

    // @GetMapping("/customer/checkout")
    // public String checkoutPage(Model model, Principal principal) {
    // String email = getAuthenticatedUserEmail();
    // // String email = principal.getName(); // Get the logged-in user's email
    // User user = cartService.getUserByEmail(email);

    // if (user == null) {
    // return "redirect:/login"; // Redirect to login if user not found
    // }
    // Cart cart = cartService.getUserCart(user);

    // if (cart == null || cart.getCartItems() == null ||
    // cart.getCartItems().isEmpty()) {
    // model.addAttribute("error", "Your cart is empty!");
    // // return "redirect:/customer/restaurant-list"; // Redirect to home page with
    // an error message
    // }

    // // Fetch the restaurant details, with null safety
    // Restaurant restaurant = cart.getCartItems()
    // .stream()
    // .filter(cartItem -> cartItem.getMenuItem() != null &&
    // cartItem.getMenuItem().getCategory() != null &&
    // cartItem.getMenuItem().getCategory().getRestaurant() != null)
    // .findFirst()
    // .map(cartItem -> cartItem.getMenuItem().getCategory().getRestaurant())
    // .orElse(null);

    // if (restaurant == null) {
    // model.addAttribute("error", "Unable to fetch restaurant details for the items
    // in your cart.");
    // // return "redirect:/customer/restaurant-list"; // Load checkout page with an
    // error message
    // }

    // model.addAttribute("cart", cart);
    // model.addAttribute("restaurant", restaurant);
    // return "check-out_copy";
    // }

    @GetMapping("/customer/checkout")
    public String checkoutPage(Model model, Principal principal) {
        String email = getAuthenticatedUserEmail();
        User user = cartService.getUserByEmail(email);

        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getUserCart(user);
        Restaurant restaurant = null;

        // Check for empty cart first
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            model.addAttribute("error", "Your cart is empty!");
        } else {
            // Only try to get restaurant if cart has items
            restaurant = cart.getCartItems()
                    .stream()
                    .filter(cartItem -> cartItem.getMenuItem() != null &&
                            cartItem.getMenuItem().getCategory() != null &&
                            cartItem.getMenuItem().getCategory().getRestaurant() != null)
                    .findFirst()
                    .map(cartItem -> cartItem.getMenuItem().getCategory().getRestaurant())
                    .orElse(null);

            if (restaurant == null) {
                model.addAttribute("error", "Unable to fetch restaurant details for the items in your cart.");
            }
        }

        // Only add cart to model if it exists and has items
        if (cart != null && cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            model.addAttribute("cart", cart);
        }

        model.addAttribute("restaurant", restaurant);
        return "check-out_copy";
    }

    @PostMapping("/customer/cart/update-quantity")
    public String updateCartQuantity(
            @RequestParam("cartItemId") Long cartItemId,
            @RequestParam("quantity") String quantityAction,
            RedirectAttributes redirectAttributes) {

        System.out.println("Cart Item ID: " + cartItemId);
        System.out.println("Quantity Action: " + quantityAction);

        try {
            // Fetch the cart item
            CartItem cartItem = cartItemService.findById(cartItemId);
            if (cartItem == null) {
                redirectAttributes.addFlashAttribute("error", "Cart item not found.");
                return "redirect:/customer/checkout";
            }

            // Update quantity
            if ("increase".equals(quantityAction)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            } else if ("decrease".equals(quantityAction) && cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid quantity action.");
                return "redirect:/customer/checkout";
            }

            // Save the updated cart item
            cartItemService.save(cartItem);

            // Update total price in the cart
            Cart cart = cartItem.getCart();
            cart.setTotalPrice(cartItemService.calculateTotalPrice(cart));
            cartService.save(cart);

            redirectAttributes.addFlashAttribute("success", "Cart updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating the cart.");
        }
        return "redirect:/customer/checkout";
    }

    @PostMapping("/customer/cart/remove-item")
    public String removeCartItem(@RequestParam("cartItemId") Long cartItemId, RedirectAttributes redirectAttributes) {
        try {
            // Find and remove the cart item
            CartItem cartItem = cartItemService.findById(cartItemId);
            if (cartItem != null) {
                cartItemService.delete(cartItem);

                // Recalculate the cart total after removal
                Cart cart = cartItem.getCart();
                cart.setTotalPrice(cartItemService.calculateTotalPrice(cart));
                cartService.save(cart);

                redirectAttributes.addFlashAttribute("success", "Item removed successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Cart item not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while removing the item.");
        }
        return "redirect:/customer/checkout";
    }

    @PostMapping("/customer/place-order")
    public String placeOrder(@RequestParam String name,
            @RequestParam String address,
            @RequestParam String phone,
            @RequestParam String paymentMethod,
            @RequestParam Long restaurantId,
            Principal principal) {

        String email = getAuthenticatedUserEmail();

        // String email = principal.getName(); // Get the logged-in user's email
        User user = cartService.getUserByEmail(email);

        if (user == null) {
            return "redirect:/login"; // Redirect to login if user is not found
        }

        Cart cart = cartService.getUserCart(user);

        // Fetch the restaurant from the database using the restaurantId
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Create the order object
        Order order = new Order();
        order.setName(name);
        order.setAddress(address);
        order.setPhoneNumber(phone);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("PENDING");
        order.setTotalPrice(cart.getTotalPrice());
        order.setRestaurant(restaurant);
        order.setOrderDate(LocalDateTime.now());
        // new change
        order.setUser(user);

        order = orderService.saveOrder(order);

        System.out.println("Order ID: " + order.getId());

        // Transfer CartItem details to OrderItem
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getMenuItem().getPrice()); // Assuming MenuItem has a price field
            orderItemService.saveOrderItem(orderItem);
        }
        // Clear the cart
        cartService.clearCart(cart);
        return "redirect:/customer/order/confirmation?orderId=" + order.getId(); // Redirect to a success page
    }

    @GetMapping("/customer/order/confirmation")
    public String showOrderConfirmation(@RequestParam Long orderId, Model model) {
        // Fetch order by ID
        Order order = orderService.getOrderById(orderId);

        // Add the order to the model
        model.addAttribute("order", order);

        return "user-order-confirmation";
    }

    @GetMapping("/customer/feedback/form/{restaurantId}/{customerId}")
    public String showFeedbackForm(@PathVariable Long restaurantId, @PathVariable Long customerId, Model model) {
        // Fetch restaurant and customer based on IDs
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        User user = userService.findById(customerId);

        // Add the restaurant and customer to the model
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("user", user);

        return "feedback_form"; // Name of your feedback form HTML new String();
    }

    @PostMapping("/customer/feedback/submit")
    public String submitFeedback(@RequestParam Long restaurantId,
            @RequestParam Long customerId,
            @RequestParam int rating,
            @RequestParam String comment) {
        // Handle feedback submission logic here

        Feedback feedback = new Feedback();
        feedback.setRating(rating);
        feedback.setComment(comment);

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        User user = userService.findById(customerId);

        feedback.setRestaurant(restaurant);
        feedback.setUser(user);

        feedbackService.saveFeedback(feedback);

        return "redirect:/customer/feedback/success"; // Redirect to success page after feedback is submitted
    }

    @GetMapping("/customer/feedback/success")
    public String feedbackSuccess() {
        return "feedback_success";
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            // Normal login (email/password)
            return authentication.getName();
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            // Google login
            OAuth2User oauthUser = ((OAuth2AuthenticationToken) authentication).getPrincipal();

            // 🔴 DEBUG: Print all attributes to verify the email key
            System.out.println("OAuth2 Attributes: " + oauthUser.getAttributes());

            return (String) oauthUser.getAttributes().get("email"); // Extract email
        }

        return null; // If no valid authentication is found
    }

}