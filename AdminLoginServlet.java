package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin-login")
public class AdminLoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User admin = userService.loginAdmin(username, password);

        if (admin != null) {

            HttpSession session = request.getSession();
            session.setAttribute("admin", admin);

            response.sendRedirect(
                    request.getContextPath() + "/admin/dashboard.jsp"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Invalid admin username or password"
            );

            request.getRequestDispatcher(
                    "/admin/login.jsp"
            ).forward(request, response);
        }
    }
}