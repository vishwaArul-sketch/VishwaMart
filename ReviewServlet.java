package com.ecommerce.controller;

import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.service.ReviewService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewService();

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

        try {

            int productId = Integer.parseInt(
                    request.getParameter("productId")
            );

            int rating = Integer.parseInt(
                    request.getParameter("rating")
            );

            String comment = request.getParameter("comment");

            if (rating < 1 || rating > 5) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/product-details?id=" + productId
                );
                return;
            }

            Review review = new Review();

            review.setUserId(user.getId());
            review.setProductId(productId);
            review.setRating(rating);
            review.setComment(comment);

            reviewService.addReview(review);

            response.sendRedirect(
                    request.getContextPath()
                    + "/product-details?id=" + productId
            );

        } catch (Exception e) {
            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath() + "/products"
            );
        }
    }
}