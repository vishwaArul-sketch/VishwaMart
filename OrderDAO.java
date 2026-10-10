package com.ecommerce.dao;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int createOrder(Order order, List<OrderItem> items) {

        String orderSql = """
                INSERT INTO orders
                (user_id, total_amount, address, payment_method, status)
                VALUES (?, ?, ?, ?, ?)
                """;

        String itemSql = """
                INSERT INTO order_items
                (order_id, product_id, quantity, price)
                VALUES (?, ?, ?, ?)
                """;

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int orderId;

            try (PreparedStatement ps = con.prepareStatement(
                    orderSql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, order.getUserId());
                ps.setDouble(2, order.getTotalAmount());
                ps.setString(3, order.getAddress());
                ps.setString(4, order.getPaymentMethod());
                ps.setString(5, order.getStatus());

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (!rs.next()) {
                    con.rollback();
                    return 0;
                }

                orderId = rs.getInt(1);
            }

            try (PreparedStatement ps = con.prepareStatement(itemSql)) {

                for (OrderItem item : items) {

                    ps.setInt(1, orderId);
                    ps.setInt(2, item.getProductId());
                    ps.setInt(3, item.getQuantity());
                    ps.setDouble(4, item.getPrice());

                    ps.addBatch();
                }

                ps.executeBatch();
            }

            con.commit();

            return orderId;

        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ignored) {
            }

            return 0;

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ignored) {
            }
        }
    }

    public List<Order> getOrdersByUser(int userId) {

        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT id, user_id, total_amount, address,
                       payment_method, status, created_at
                FROM orders
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Order order = new Order();

                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setAddress(rs.getString("address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));

                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT id, user_id, total_amount, address,
                       payment_method, status, created_at
                FROM orders
                ORDER BY created_at DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Order order = new Order();

                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setAddress(rs.getString("address"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setStatus(rs.getString("status"));
                order.setCreatedAt(rs.getTimestamp("created_at"));

                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public boolean updateOrderStatus(int orderId, String status) {

        String sql = """
                UPDATE orders
                SET status = ?
                WHERE id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}