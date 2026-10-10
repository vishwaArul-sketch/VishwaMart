package com.ecommerce.dao;

import com.ecommerce.model.Review;
import com.ecommerce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public boolean addReview(Review review) {

        String sql = "INSERT INTO reviews " +
                     "(user_id, product_id, rating, comment) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, review.getUserId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Review> getReviewsByProduct(int productId) {

        List<Review> reviews = new ArrayList<>();

        String sql = "SELECT r.*, u.name AS user_name " +
                     "FROM reviews r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "WHERE r.product_id = ? " +
                     "ORDER BY r.created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Review review = new Review();

                    review.setId(rs.getInt("id"));
                    review.setUserId(rs.getInt("user_id"));
                    review.setProductId(rs.getInt("product_id"));
                    review.setRating(rs.getInt("rating"));
                    review.setComment(rs.getString("comment"));
                    review.setCreatedAt(
                            rs.getTimestamp("created_at")
                    );
                    review.setUserName(
                            rs.getString("user_name")
                    );

                    reviews.add(review);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }
}