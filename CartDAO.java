package com.ecommerce.dao;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Product;
import com.ecommerce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public boolean addToCart(int userId, int productId, int quantity) {

        String sql = "INSERT INTO cart (user_id, product_id, quantity) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, quantity);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cart> getCartByUser(int userId) {

        List<Cart> cartList = new ArrayList<>();

        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, " +
                     "p.name, p.description, p.price, p.quantity AS stock, " +
                     "p.category_id, p.image, " +
                     "cat.name AS category_name " +
                     "FROM cart c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "LEFT JOIN categories cat ON p.category_id = cat.id " +
                     "WHERE c.user_id = ? " +
                     "ORDER BY c.id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Cart cart = new Cart();

                    cart.setId(rs.getInt("id"));
                    cart.setUserId(rs.getInt("user_id"));
                    cart.setProductId(rs.getInt("product_id"));
                    cart.setQuantity(rs.getInt("quantity"));

                    Product product = new Product();

                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQuantity(rs.getInt("stock"));
                    product.setCategoryId(rs.getInt("category_id"));
                    product.setCategoryName(rs.getString("category_name"));
                    product.setImage(rs.getString("image"));

                    cart.setProduct(product);

                    cartList.add(cart);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartList;
    }

    public boolean updateQuantity(int userId, int productId, int quantity) {

        String sql = "UPDATE cart SET quantity = ? " +
                     "WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromCart(int userId, int productId) {

        String sql = "DELETE FROM cart " +
                     "WHERE user_id = ? AND product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearCart(int userId) {

        String sql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() >= 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}