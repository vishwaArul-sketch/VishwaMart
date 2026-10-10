package com.ecommerce.dao;

import com.ecommerce.model.Product;
import com.ecommerce.model.Wishlist;
import com.ecommerce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    public boolean addToWishlist(int userId, int productId) {

        String sql = "INSERT INTO wishlist (user_id, product_id) " +
                     "VALUES (?, ?) " +
                     "ON DUPLICATE KEY UPDATE product_id = product_id";

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

    public List<Wishlist> getWishlistByUser(int userId) {

        List<Wishlist> wishlist = new ArrayList<>();

        String sql = "SELECT w.id, w.user_id, w.product_id, " +
                     "p.name, p.description, p.price, p.quantity, " +
                     "p.category_id, p.image, " +
                     "c.name AS category_name " +
                     "FROM wishlist w " +
                     "JOIN products p ON w.product_id = p.id " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE w.user_id = ? " +
                     "ORDER BY w.id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Wishlist item = new Wishlist();

                    item.setId(rs.getInt("id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProductId(rs.getInt("product_id"));

                    Product product = new Product();

                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setCategoryId(rs.getInt("category_id"));
                    product.setCategoryName(rs.getString("category_name"));
                    product.setImage(rs.getString("image"));

                    item.setProduct(product);

                    wishlist.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wishlist;
    }

    public boolean removeFromWishlist(int userId, int productId) {

        String sql = "DELETE FROM wishlist " +
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
}