package models;

import DAL.Category;
import DTO.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PQT2212
 */
public class CategoryDAO{

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public List<Category> getCategory() {
        List<Category> categories = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Categories";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int categoryID = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");

                categories.add(new Category(categoryID, categoryName));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return categories;
    }
    public Category getCategoryByCategoryID(int id) {
        Category category = null;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Categories WHERE CategoryID =?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int categoryID = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");

                category = new Category(categoryID, categoryName);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return category;
    }
}
