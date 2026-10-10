package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.model.Wishlist;
import com.ecommerce.service.WishlistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();

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

        List<Wishlist> wishlist =
                wishlistService.getWishlistByUser(user.getId());

        request.setAttribute("wishlist", wishlist);

        request.getRequestDispatcher(
                "/user/wishlist.jsp"
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

                wishlistService.addToWishlist(
                        user.getId(),
                        productId
                );

            } else if ("remove".equals(action)) {

                wishlistService.removeFromWishlist(
                        user.getId(),
                        productId
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(
                request.getContextPath() + "/wishlist"
        );
    }
}