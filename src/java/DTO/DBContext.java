package DTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author FPT University - PRJ301
 */
public class DBContext {
    private static DBContext instance;
    private Connection connection;
    public DBContext()
    {
        //@Students: You are allowed to edit user, pass, url variables to fit 
        //your system configuration
        //You can also add more methods for Database Interaction tasks. 
        //But we recommend you to do it in another class
        // For example : StudentDBContext extends DBContext , 
        //where StudentDBContext is located in dal package, 
        
        //B1: Tao va mo dtuong knoi
        //B2: Tao doi tuong PrepareStatement thực thi truy vấn
        //B3: Thuc thi truy van:    - select > executeQuery() > return ResultSet    - Insert, update, delete, > executeUpdate() > return int
		
        try {
            String user = "sa";
            String pass = "123456";
            String url = "jdbc:sqlserver://MYLAP2212\\SQLEXPRESS:1433;databaseName=PRJ301_DB";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Connection getConnection() {
        return connection;
    }

    public static DBContext getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
                instance = new DBContext();
        }
        return instance;
    }

    public static void releaseJBDCObject(ResultSet rs,PreparedStatement ps,Connection conn){
        try {
            if(rs!= null) {
                rs.close();
            }
            if(ps!=null) {
                ps.close();
            }
            if(conn!=null) {
                conn.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(DBContext.class.getName()).log(Level.ALL,e.toString(),e);
        }
    }
}
