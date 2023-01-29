/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import DAL.Category;
import DTO.DBContext;
import DAL.Product;
import DTO.ProductCategory;
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
public class ProductDAO {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public List<Product> getTop4BestSaleProduct() {
        List<Product> p = new ArrayList<>();

        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "  SELECT TOP(4) b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  FROM [Order Details] a inner join Products b ON a.ProductID=b.ProductID \n"
                    + "  GROUP BY b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  ORDER BY count(a.OrderID) DESC"; //, SUM(a.Quantity) DESC
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");

                Product product = new Product(productID, productName, unitPrice);

                p.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public List<Product> getTop4HotProduct() {
        List<Product> p = new ArrayList<>();

        try {
            connection = DBContext.getInstance().getConnection();
            String sql = " SELECT TOP(4) b.ProductID,b.ProductName,b.UnitPrice,c.Discount\n"
                    + "    FROM Products b inner join  [Order Details] c on b.ProductID = c.ProductID\n"
                    + "    WHERE b.UnitPrice>0 \n"
                    + "    GROUP BY b.ProductID,b.ProductName,b.UnitPrice,c.Discount\n"
                    + "    ORDER BY c.Discount DESC"; //, b.UnitPrice ASC
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");

                Product product = new Product(productID, productName, unitPrice);

                p.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public List<Product> getTop4NewProduct() {
        List<Product> p = new ArrayList<>();

        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "  SELECT TOP(4) b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  FROM Products b \n"
                    + "  WHERE b.UnitPrice>0"
                    + "  GROUP BY b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  ORDER BY b.ProductID DESC";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");

                Product product = new Product(productID, productName, unitPrice);

                p.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public List<Product> getTop4NewBuyProductByCustomerID(String id) {
        List<Product> p = new ArrayList<>();

        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "  SELECT TOP(4) b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  FROM Products b \n"
                    + "  WHERE b.UnitPrice>0"
                    + "  GROUP BY b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  ORDER BY b.ProductID DESC";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");

                Product product = new Product(productID, productName, unitPrice);

                p.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public List<Product> getTop4BestSaleProductByCategory(int id) {
        List<Product> p = new ArrayList<>();

        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "  SELECT TOP(4) b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  FROM [Order Details] a inner join Products b ON a.ProductID=b.ProductID \n"
                    + "  WHERE b.CategoryID=?\n"
                    + "  GROUP BY b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  ORDER BY count(a.OrderID) DESC"; //, SUM(a.Quantity) DESC
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");

                Product product = new Product(productID, productName, unitPrice);

                p.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public List<Product> getTop4NewProductByCategory(int id) {
        List<Product> p = new ArrayList<>();

        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "  SELECT TOP(4) b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  FROM Products b \n"
                    + "  WHERE b.UnitPrice>0 AND b.CategoryID=?"
                    + "  GROUP BY b.ProductID,b.ProductName,b.UnitPrice\n"
                    + "  ORDER BY b.ProductID DESC";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");

                Product product = new Product(productID, productName, unitPrice);

                p.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public List<Product> getTop4HotProductByCategory(int id) {
        List<Product> p = new ArrayList<>();

        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT TOP(4) b.ProductID,b.ProductName,b.UnitPrice,c.Discount\n"
                    + "    FROM Products b inner join  [Order Details] c on b.ProductID = c.ProductID\n"
                    + "    WHERE b.UnitPrice>0 AND b.CategoryID=? \n"
                    + "    GROUP BY b.ProductID,b.ProductName,b.UnitPrice,c.Discount\n"
                    + "    ORDER BY c.Discount DESC"; //, b.UnitPrice ASC
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                double unitPrice = rs.getDouble("UnitPrice");

                Product product = new Product(productID, productName, unitPrice);

                p.add(product);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public ProductCategory getProductCategoryByProductID(int id) {
        ProductCategory p = null;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "select *  from Products as a , Categories as b  where a.CategoryID=b.CategoryID AND a.ProductID=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                String quantityPerUnit = rs.getString("QuantityPerUnit");
                double unitPrice = rs.getDouble("UnitPrice");
                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");
                int reorderLevel = rs.getInt("ReorderLevel");
                boolean discontinued = rs.getBoolean("Discontinued");

                String categoryName = rs.getString("CategoryName");
                Product product = new Product(productID, productName, categoryID, quantityPerUnit, unitPrice, unitsInStock, unitsOnOrder, reorderLevel, discontinued);
                Category category = new Category(categoryID, categoryName);

                p = new ProductCategory(product, category);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public List<ProductCategory> getProductCategory() {
        List<ProductCategory> p = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "select *  from Products as a , Categories as b  where a.CategoryID=b.CategoryID";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                String quantityPerUnit = rs.getString("QuantityPerUnit");
                double unitPrice = rs.getDouble("UnitPrice");
                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");
                int reorderLevel = rs.getInt("ReorderLevel");
                boolean discontinued = rs.getBoolean("Discontinued");

                String categoryName = rs.getString("CategoryName");
                Product product = new Product(productID, productName, categoryID, quantityPerUnit, unitPrice, unitsInStock, unitsOnOrder, reorderLevel, discontinued);
                Category category = new Category(categoryID, categoryName);

                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public Product getProductByProductID(int id) {
        Product product = null;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "select *  from Products  where ProductID=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                String quantityPerUnit = rs.getString("QuantityPerUnit");
                double unitPrice = rs.getDouble("UnitPrice");
                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");
                int reorderLevel = rs.getInt("ReorderLevel");
                boolean discontinued = rs.getBoolean("Discontinued");

                product = new Product(productID, productName, categoryID, quantityPerUnit, unitPrice, unitsInStock, unitsOnOrder, reorderLevel, discontinued);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return product;
    }

    public List<ProductCategory> getProductCategoryByProductName(String name) {
        List<ProductCategory> p = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();

            String sql = "SELECT * FROM Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID WHERE a.ProductName LIKE '%'+?+'%'";
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                String quantityPerUnit = rs.getString("QuantityPerUnit");
                double unitPrice = rs.getDouble("UnitPrice");
                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");
                int reorderLevel = rs.getInt("ReorderLevel");
                boolean discontinued = rs.getBoolean("Discontinued");

                Product product = new Product(productID, productName, categoryID, quantityPerUnit, unitPrice, unitsInStock, unitsOnOrder, reorderLevel, discontinued);

                String categoryName = rs.getString("CategoryName");
                Category category = new Category(categoryID, categoryName);

                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public List<ProductCategory> getProductCategoryByCategoryID(int id) {
        List<ProductCategory> p = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();

            String sql = "SELECT * \n"
                    + "FROM Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID WHERE a.CategoryID = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                String quantityPerUnit = rs.getString("QuantityPerUnit");
                double unitPrice = rs.getDouble("UnitPrice");
                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");
                int reorderLevel = rs.getInt("ReorderLevel");
                boolean discontinued = rs.getBoolean("Discontinued");

                Product product = new Product(productID, productName, categoryID, quantityPerUnit, unitPrice, unitsInStock, unitsOnOrder, reorderLevel, discontinued);

                String categoryName = rs.getString("CategoryName");
                Category category = new Category(categoryID, categoryName);

                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }

    public int update(Product p) throws SQLException {
        int check = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "update Products SET ProductName = ?, CategoryID = ?, "
                    + "QuantityPerUnit = ?, UnitPrice = ?, UnitsInStock = ?, Discontinued = ? where ProductID = ?";

            ps = connection.prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryID());
            ps.setString(3, p.getQuantityPerUnit());
            ps.setDouble(4, p.getUnitPrice());
            ps.setInt(5, p.getUnitsInStock());
            ps.setBoolean(6, p.isDiscontinued());
            ps.setInt(7, p.getProductID());

            check = ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
            return 0;
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return check;
    }

    public int Delete(int ID) throws SQLException {
        String sql1 = "DELETE FROM Products WHERE ProductID = ?";
        int check = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(sql1);
            ps.setInt(1, ID);
            check = ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
            return 0;
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return check;
    }

    public int CreateProduct(Product p) throws SQLException {
        String sql = "insert into Products(ProductName,CategoryID,QuantityPerUnit,UnitPrice,"
                + "UnitsInStock,UnitsOnOrder,ReorderLevel,Discontinued)\n"
                + "values(?,?,?,?,?,?,?,?)";
        int check = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            ps = connection.prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryID());
            ps.setString(3, p.getQuantityPerUnit());
            ps.setDouble(4, p.getUnitPrice());
            ps.setInt(5, p.getUnitsInStock());
            ps.setInt(6, 0);
            ps.setInt(7, 0);
            ps.setBoolean(8, p.isDiscontinued());
            check = ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
            return 0;
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return check;
    }
    /*
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    public List<ProductCategory> getProductCategory() {     
        List<ProductCategory> p = new ArrayList<>();
        
        try {
            connection = DBContext.getInstance().getConnection();
            String sql =    "select * from Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                double unitPrice = rs.getDouble("UnitPrice");       
                
                String categoryName = rs.getString("CategoryName");  
                Product product = new Product(productID, productName, categoryID, unitPrice);
                Category category = new Category(categoryID, categoryName);
                
                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public List<ProductCategory> getProductCategoryByProductName(String name) {     
        List<ProductCategory> p = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            
            String sql =    "SELECT * FROM Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID WHERE a.ProductName LIKE '%'+?+'%'";
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                double unitPrice = rs.getDouble("UnitPrice");       
                
                String categoryName = rs.getString("CategoryName");  
                Product product = new Product(productID, productName, categoryID, unitPrice);
                Category category = new Category(categoryID, categoryName);
                
                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public List<ProductCategory> getProductCategoryByCategoryID(int id) {     
        List<ProductCategory> p = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            
            String sql =    "SELECT * \n" +
                            "FROM Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID WHERE a.CategoryID = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                double unitPrice = rs.getDouble("UnitPrice");       
                
                String categoryName = rs.getString("CategoryName");  
                Product product = new Product(productID, productName, categoryID, unitPrice);
                Category category = new Category(categoryID, categoryName);
                
                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public ProductCategory getProductCategoryByProductID(int id) {     
        ProductCategory p = null;
        try {
            connection = DBContext.getInstance().getConnection();
            
            String sql = "select *  from Products as a , Categories as b  where a.CategoryID=b.CategoryID AND a.ProductID=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                String quantityPerUnit = rs.getString("QuantityPerUnit");
                double unitPrice = rs.getDouble("UnitPrice");
                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");
                int reorderLevel = rs.getInt("ReorderLevel");
                boolean discontinued = rs.getBoolean("Discontinued");       
                
                String categoryName = rs.getString("CategoryName");  
                Product product = new Product(productID, productName, categoryID, quantityPerUnit, unitPrice, unitsInStock, unitsOnOrder, reorderLevel, discontinued);
                Category category = new Category(categoryID, categoryName);
                
                p = new ProductCategory(product, category);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public int update(Product p) throws SQLException {
        int check=0;
        try {
            connection = DBContext.getInstance().getConnection();
            
            String sql = "update Products SET ProductName = ?, CategoryID = ?, "
                    + "QuantityPerUnit = ?, UnitPrice = ?, UnitsInStock = ?, UnitsOnOrder = ?, "
                    + "ReorderLevel = ?, Discontinued = ? where ProductID = ?";

            ps = connection.prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryID());
            ps.setString(3, p.getQuantityPerUnit());
            ps.setDouble(4, p.getUnitPrice());
            ps.setInt(5, p.getUnitsInStock());
            ps.setInt(6, p.getUnitsOnOrder());
            ps.setInt(7, p.getReorderLevel());
            ps.setBoolean(8, p.isDiscontinued());
            ps.setInt(9, p.getProductID());
            
            check = ps.executeUpdate();
        } catch (SQLException e) {
            connection.rollback();
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return check;
    }
    
    public int Delete(int ID) throws SQLException {
        String sql1 = "DELETE FROM Products WHERE ProductID = ?";
        int check=0;
        try {
            connection = DBContext.getInstance().getConnection();
            
            ps = connection.prepareStatement(sql1);           
            ps.setInt(1, ID);
            check = ps.executeUpdate();
        } catch (SQLException e) {
            connection.rollback();
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return check;
    }
    public int CreateProduct(Product p) throws SQLException {
        String sql = "insert into Products(ProductName,CategoryID,QuantityPerUnit,UnitPrice,"
                + "UnitsInStock,UnitsOnOrder,ReorderLevel,Discontinued)\n"
                + "values(?,?,?,?,?,?,?,?)";
        int check=0;
        try {            
            connection = DBContext.getInstance().getConnection();
            
            ps = connection.prepareStatement(sql);
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryID());
            ps.setString(3, p.getQuantityPerUnit());
            ps.setDouble(4, p.getUnitPrice());
            ps.setInt(5, p.getUnitsInStock());
            ps.setInt(6, p.getUnitsOnOrder());
            ps.setInt(7, p.getReorderLevel());
            ps.setBoolean(8, p.isDiscontinued());
            check = ps.executeUpdate();
        } catch (SQLException e) {
            connection.rollback();
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return check;
    }
    /*
    public List<ProductCategory> getProductCategoryWithPagination(int page, int number) {     
        List<ProductCategory> p = new ArrayList<>();
        
        try {
            connection = DBContext.getInstance().getConnection();
            String sql =    "SELECT * \n" +
                            "FROM (select ROW_NUMBER() OVER(ORDER BY a.ProductID ) AS Row,a.*,b.CategoryName\n" +
                            "		from Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID)as d \n" +
                            "where d.Row >=? and d.Row <=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, (page-1)*number+1);
            ps.setInt(2, number*page);
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                double unitPrice = rs.getDouble("UnitPrice");       
                
                String categoryName = rs.getString("CategoryName");  
                Product product = new Product(productID, productName, categoryID, unitPrice);
                Category category = new Category(categoryID, categoryName);
                
                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public int getTotalPageOfProductCategory(int number) {
        double count = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String rowCount = "SELECT COUNT(c.ProductID)as row FROM (select *  from Products) as c";
            ps = connection.prepareStatement(rowCount);
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("row");
            }                      
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return (int) Math.ceil(count/number);
    }

    public List<ProductCategory> getProductCategoryByProductName(String name, int page, int number) {     
        List<ProductCategory> p = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            
            String sql =    "SELECT * \n" +
                            "FROM (select ROW_NUMBER() OVER(ORDER BY a.ProductID ) AS Row,a.*,b.CategoryName\n" +
                            "		from Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID WHERE a.ProductName LIKE '%'+?+'%')as d \n" +
                            "where d.Row >=? and d.Row <=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, (page-1)*number+1);
            ps.setInt(3, number*(page));
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                double unitPrice = rs.getDouble("UnitPrice");       
                
                String categoryName = rs.getString("CategoryName");  
                Product product = new Product(productID, productName, categoryID, unitPrice);
                Category category = new Category(categoryID, categoryName);
                
                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public int getTotalPageOfProductCategoryByProductName(String name, int number) {     
        double count = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String rowCount = "SELECT COUNT(d.ProductID)as row FROM (select *  from Products as a where a.ProductName like  '%'+?+'%')as d";
            ps = connection.prepareStatement(rowCount);
            ps.setString(1, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("row");
            }                      
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return (int) Math.ceil(count/number);
    }
    public List<ProductCategory> getProductCategoryByCategoryID(int id, int page, int number) {     
        List<ProductCategory> p = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            
            String sql =    "SELECT * \n" +
                            "FROM (select ROW_NUMBER() OVER(ORDER BY a.ProductID ) AS Row,a.*,b.CategoryName\n" +
                            "		from Products a INNER JOIN Categories b ON a.CategoryID=b.CategoryID WHERE a.CategoryID = ?)as d \n" +
                            "where d.Row >=? and d.Row <=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, (page-1)*number+1);
            ps.setInt(3, number*(page));
            rs = ps.executeQuery();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int categoryID = rs.getInt("CategoryID");
                double unitPrice = rs.getDouble("UnitPrice");       
                
                String categoryName = rs.getString("CategoryName");  
                Product product = new Product(productID, productName, categoryID, unitPrice);
                Category category = new Category(categoryID, categoryName);
                
                p.add(new ProductCategory(product, category));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return p;
    }
    public int getTotalPageOfProductCategoryByCategoryID(int id, int number) {     
        double count = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String rowCount = "SELECT COUNT(d.ProductID)as row FROM (select *  from Products as a where a.CategoryID = ?)as d";
            ps = connection.prepareStatement(rowCount);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("row");
            }                      
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING,e.getMessage(),e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return (int) Math.ceil(count/number);
    }
     */
}
