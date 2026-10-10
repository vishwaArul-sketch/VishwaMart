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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.loginUser(email, password);

        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            response.sendRedirect(
                    request.getContextPath() + "/user/home.jsp"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Invalid email or password"
            );

            request.getRequestDispatcher(
                    "/user/login.jsp"
            ).forward(request, response);
        }
    }
}