package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/login.jsp"
            );
            return;
        }

        List<Order> orders = orderService.getAllOrders();

        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/admin/orders.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/login.jsp"
            );
            return;
        }

        try {

            int orderId =
                    Integer.parseInt(request.getParameter("orderId"));

            String status =
                    request.getParameter("status");

            orderService.updateOrderStatus(orderId, status);

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/orders"
        );
    }
}