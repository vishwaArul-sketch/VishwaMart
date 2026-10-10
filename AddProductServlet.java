package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/add-product")
public class AddProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceValue = request.getParameter("price");
        String quantityValue = request.getParameter("quantity");
        String categoryIdValue = request.getParameter("categoryId");
        String image = request.getParameter("image");

        try {

            double price = Double.parseDouble(priceValue);
            int quantity = Integer.parseInt(quantityValue);
            int categoryId = Integer.parseInt(categoryIdValue);

            Product product = new Product();

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setCategoryId(categoryId);
            product.setImage(image);

            boolean success = productService.addProduct(product);

            if (success) {
                response.sendRedirect(
                    request.getContextPath() + "/admin/products.jsp"
                );
            } else {
                request.setAttribute(
                    "error",
                    "Product could not be added"
                );

                request.getRequestDispatcher(
                    "/admin/add-product.jsp"
                ).forward(request, response);
            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                "error",
                "Invalid product details"
            );

            request.getRequestDispatcher(
                "/admin/add-product.jsp"
            ).forward(request, response);
        }
    }
}