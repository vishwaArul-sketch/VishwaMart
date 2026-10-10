package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/user/login.jsp"
            );
            return;
        }

        User user = (User) session.getAttribute("user");

        request.setAttribute(
                "orders",
                orderService.getOrdersByUser(user.getId())
        );

        request.getRequestDispatcher(
                "/user/orders.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/user/login.jsp"
            );
            return;
        }

        User user = (User) session.getAttribute("user");

        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");

        if (address == null || address.trim().isEmpty() ||
            paymentMethod == null || paymentMethod.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please provide address and payment method"
            );

            request.getRequestDispatcher(
                    "/user/checkout.jsp"
            ).forward(request, response);

            return;
        }

        List<Cart> cartList =
                cartService.getCartByUser(user.getId());

        if (cartList.isEmpty()) {

            response.sendRedirect(
                    request.getContextPath() + "/cart"
            );

            return;
        }

        List<OrderItem> items = new ArrayList<>();

        double total = 0;

        for (Cart cart : cartList) {

            OrderItem item = new OrderItem();

            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getProduct().getPrice());

            items.add(item);

            total += cart.getProduct().getPrice()
                    * cart.getQuantity();
        }

        Order order = new Order();

        order.setUserId(user.getId());
        order.setTotalAmount(total);
        order.setAddress(address.trim());
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PLACED");

        int orderId =
                orderService.createOrder(order, items);

        if (orderId > 0) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/orders?success=true"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Unable to place order. Please check product stock."
            );

            request.getRequestDispatcher(
                    "/user/checkout.jsp"
            ).forward(request, response);
        }
    }
}