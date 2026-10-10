package com.ecommerce.controller;

import com.ecommerce.model.Cart;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

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

        List<Cart> cartList =
                cartService.getCartByUser(user.getId());

        request.setAttribute("cartList", cartList);

        request.getRequestDispatcher(
                "/user/cart.jsp"
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

        String action = request.getParameter("action");

        try {

            int productId = Integer.parseInt(
                    request.getParameter("productId")
            );

            if ("add".equals(action)) {

                int quantity = Integer.parseInt(
                        request.getParameter("quantity")
                );

                if (quantity < 1) {
                    quantity = 1;
                }

                cartService.addToCart(
                        user.getId(),
                        productId,
                        quantity
                );

            } else if ("update".equals(action)) {

                int quantity = Integer.parseInt(
                        request.getParameter("quantity")
                );

                if (quantity < 1) {
                    quantity = 1;
                }

                cartService.updateQuantity(
                        user.getId(),
                        productId,
                        quantity
                );

            } else if ("remove".equals(action)) {

                cartService.removeFromCart(
                        user.getId(),
                        productId
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(
                request.getContextPath() + "/cart"
        );
    }
}