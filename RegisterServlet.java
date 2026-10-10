package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        if (name == null || username == null || email == null ||
            password == null || confirmPassword == null) {

            request.setAttribute("error", "Please fill all required fields");
            request.getRequestDispatcher("/user/register.jsp")
                   .forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {

            request.setAttribute("error", "Password does not match");
            request.getRequestDispatcher("/user/register.jsp")
                   .forward(request, response);
            return;
        }

        User user = new User();

        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");
        user.setPhone(phone);
        user.setAddress(address);

        boolean registered = userService.registerUser(user);

        if (registered) {

            response.sendRedirect(
                    request.getContextPath() + "/user/login.jsp"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Username or email already exists"
            );

            request.getRequestDispatcher(
                    "/user/register.jsp"
            ).forward(request, response);
        }
    }
}