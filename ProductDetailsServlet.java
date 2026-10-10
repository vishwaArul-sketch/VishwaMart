package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String idParameter = request.getParameter("id");

        if (idParameter == null || idParameter.isEmpty()) {
            response.sendRedirect(
                    request.getContextPath() + "/products"
            );
            return;
        }

        try {
            int id = Integer.parseInt(idParameter);

            Product product = productService.getProductById(id);

            if (product != null) {
                request.setAttribute("product", product);

                request.getRequestDispatcher(
                        "/user/product-details.jsp"
                ).forward(request, response);

            } else {
                response.sendRedirect(
                        request.getContextPath() + "/products"
                );
            }

        } catch (NumberFormatException e) {
            response.sendRedirect(
                    request.getContextPath() + "/products"
            );
        }
    }
}