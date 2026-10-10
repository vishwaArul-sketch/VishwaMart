package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String categoryParam = request.getParameter("category");

        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {

            products = productService.searchProducts(search);

        } else if (categoryParam != null && !categoryParam.trim().isEmpty()) {

            try {
                int categoryId = Integer.parseInt(categoryParam);
                products = productService.getProductsByCategory(categoryId);

            } catch (NumberFormatException e) {
                products = productService.getAllProducts();
            }

        } else {

            products = productService.getAllProducts();
        }

        request.setAttribute("products", products);
        request.setAttribute("search", search);

        request.getRequestDispatcher("/user/product.jsp")
               .forward(request, response);
    }
}