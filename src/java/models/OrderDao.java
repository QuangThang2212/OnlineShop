/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import DAL.Customers;
import DTO.DBContext;
import DAL.Employee;
import DAL.Order;
import DTO.OrderCustomerEmployee;
import DAL.OrderDetail;
import DTO.OrderDetailProduct;
import DAL.Product;
import DTO.AccountCustomer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PQT2212
 */
public class OrderDao {

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public int insertOrder(Order order, HashMap<Product, Integer> orderDetailList) throws SQLException {
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        int orderID;
        String addressInfor[] = order.getShipAddress().split(", ");
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sqlOrder = "insert into Orders values(?,null,?,?,null,?,?,?,?,null,null,?,?,?)";
            ps = connection.prepareStatement(sqlOrder, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, order.getCustomerID());
            ps.setDate(2, order.getOrderDate());
            ps.setDate(3, order.getRequiredDate());
            ps.setFloat(4, order.getFreight());
            ps.setString(5, order.getShipName());
            ps.setString(6, addressInfor[0]);
            ps.setString(7, addressInfor[1]);
            ps.setString(8, addressInfor[2]);
            ps.setString(9, order.getStatus());
            ps.setString(10, order.getPaymentMethod());
            int ordeorCreate = ps.executeUpdate();
           
            if (ordeorCreate == 0) {
                return 0;
            } else {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    orderID = generatedKeys.getInt(1);
                } else {
                    return 0;
                }
            }
            String sqlOrderDetail = "insert into [Order Details] values(?,?,?,?,?)";
            String sqlProductStockUpdate = "UPDATE Products SET UnitsInStock = ?,UnitsOnOrder = ? WHERE ProductID=?";
            ps2 = connection.prepareStatement(sqlOrderDetail);
            ps3 = connection.prepareStatement(sqlProductStockUpdate);
            OrderDetail orderDetail;
            for (Map.Entry<Product, Integer> ordt : orderDetailList.entrySet()) {
                orderDetail = new OrderDetail(orderID, ordt.getKey().getProductID(), ordt.getKey().getUnitPrice(), ordt.getValue(), 0);

                ps2.setInt(1, orderDetail.getOrderID());
                ps2.setInt(2, orderDetail.getProductID());
                ps2.setDouble(3, orderDetail.getUnitPrice());
                ps2.setInt(4, orderDetail.getQuantity());
                ps2.setFloat(5, orderDetail.getDiscount());
                ps2.addBatch();

                ps3.setDouble(1, ordt.getKey().getUnitsInStock() - ordt.getValue());
                ps3.setInt(2, ordt.getKey().getUnitsOnOrder() + ordt.getValue());
                ps3.setInt(3, ordt.getKey().getProductID());
                ps3.addBatch();
            }
            ps2.executeBatch();
            ps3.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
            return 0;
        } finally {
            if (ps2 != null) {
                ps2.close();
            }
            if (ps3 != null) {
                ps3.close();
            }
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orderID;
    }

    public int updateCancelOrderByOrderID(int id, AccountCustomer account) throws SQLException {
        PreparedStatement psUpdateProduct = null;
        PreparedStatement psUpdateOrder = null;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            //get list of order detail inner join product
            String sqlGetOrderDetail = "SELECT * FROM [Order Details] a INNER JOIN Products b ON a.ProductID = b.ProductID WHERE OrderID=?";
            ps = connection.prepareStatement(sqlGetOrderDetail);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            List<OrderDetailProduct> orderDetailProducts = new ArrayList<>();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                int quantity = rs.getInt("Quantity");

                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");

                orderDetailProducts.add(new OrderDetailProduct(new OrderDetail(productID, quantity), new Product(unitsInStock, unitsOnOrder)));
            }
            if (orderDetailProducts.isEmpty()) {
                return 0;
            }

            //update product
            String sqlUpdateProduct = "UPDATE Products SET UnitsInStock = ?, UnitsOnOrder = ? WHERE ProductID = ?";
            psUpdateProduct = connection.prepareStatement(sqlUpdateProduct);

            for (OrderDetailProduct ordPr : orderDetailProducts) {
                psUpdateProduct.setInt(1, ordPr.getProduct().getUnitsInStock() + ordPr.getOrderDetail().getQuantity());
                psUpdateProduct.setInt(2, ordPr.getProduct().getUnitsOnOrder() - ordPr.getOrderDetail().getQuantity());
                psUpdateProduct.setInt(3, ordPr.getOrderDetail().getProductID());
                psUpdateProduct.addBatch();
            }
            psUpdateProduct.executeBatch();

            //delte order
            String sqlDeleteOrder = "UPDATE Orders SET Status = ?, EmployeeID = ?  WHERE OrderID=?";
            psUpdateOrder = connection.prepareStatement(sqlDeleteOrder);
            psUpdateOrder.setString(1, "Order canceled");
            if(account.getAccount().getRole()==1){
                psUpdateOrder.setInt(2, account.getEmployee().getEmployeeID());
            }else{
                psUpdateOrder.setNull(2, Types.INTEGER);
            }
            psUpdateOrder.setInt(3, id);
            int deleteOrderCheck = psUpdateOrder.executeUpdate();
            if (deleteOrderCheck == 0) {
                return 0;
            }

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
            return 0;
        } finally {
            if (psUpdateProduct != null) {
                psUpdateProduct.close();
            }
            if (psUpdateOrder != null) {
                psUpdateOrder.close();
            }
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return 1;
    }

    public int updateCompleteOrderByOrderID(int orderid, int employeeid) throws SQLException {
        int completeOrderCheck = 0;
        PreparedStatement psUpdateProduct = null;
        try {
            connection = DBContext.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sqlGetOrderDetail = "SELECT * FROM [Order Details] a INNER JOIN Products b ON a.ProductID = b.ProductID WHERE OrderID=?";
            ps = connection.prepareStatement(sqlGetOrderDetail);
            ps.setInt(1, orderid);
            rs = ps.executeQuery();
            List<OrderDetailProduct> orderDetailProducts = new ArrayList<>();
            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                int quantity = rs.getInt("Quantity");

                int unitsInStock = rs.getInt("UnitsInStock");
                int unitsOnOrder = rs.getInt("UnitsOnOrder");

                orderDetailProducts.add(new OrderDetailProduct(new OrderDetail(productID, quantity), new Product(unitsInStock, unitsOnOrder)));
            }
            if (orderDetailProducts.isEmpty()) {
                return 0;
            }

            //update product
            String sqlUpdateProduct = "UPDATE Products SET UnitsOnOrder = ? WHERE ProductID = ?";
            psUpdateProduct = connection.prepareStatement(sqlUpdateProduct);

            for (OrderDetailProduct ordPr : orderDetailProducts) {
                psUpdateProduct.setInt(1, ordPr.getProduct().getUnitsOnOrder() - ordPr.getOrderDetail().getQuantity());
                psUpdateProduct.setInt(2, ordPr.getOrderDetail().getProductID());
                psUpdateProduct.addBatch();
            }
            psUpdateProduct.executeBatch();

            String sqlDeleteOrder = "UPDATE Orders SET Status = ?, ShippedDate =?, EmployeeID = ? WHERE OrderID=?";
            ps = connection.prepareStatement(sqlDeleteOrder);
            ps.setString(1, "Completed");
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setInt(3, employeeid);
            ps.setInt(4, orderid);
            completeOrderCheck = ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
            connection.rollback();
        } finally {
            if (psUpdateProduct != null) {
                psUpdateProduct.close();
            }
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return completeOrderCheck;
    }

    public List<Order> getOrderByCustomerIDAndStatus(String id, String status) {
        List<Order> orders = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Orders WHERE customerID = ? AND Status = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.setString(2, status);
            rs = ps.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                String customerID = rs.getString("CustomerID");
                int employeeID = rs.getInt("EmployeeID");
                Date orderDate = rs.getDate("OrderDate");
                Date requiredDate = rs.getDate("RequiredDate");
                Date shippedDate = rs.getDate("ShippedDate");
                float freight = rs.getFloat("Freight");
                String shipName = rs.getString("ShipName");
                String shipAddress = rs.getString("ShipAddress");
                String Status = rs.getString("Status");
                String PaymentMethod = rs.getString("PaymentMethod");

                orders.add(new Order(orderID, customerID, employeeID, orderDate, requiredDate, shippedDate, freight, shipName, shipAddress, Status, PaymentMethod));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orders;
    }

    public Order getOrderByOrderID(int id) {
        Order orders = null;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "select * from Orders a WHERE a.OrderID = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                String customerID = rs.getString("CustomerID");
                int employeeID = rs.getInt("EmployeeID");
                Date orderDate = rs.getDate("OrderDate");
                Date requiredDate = rs.getDate("RequiredDate");
                Date shippedDate = rs.getDate("ShippedDate");
                float freight = rs.getFloat("Freight");
                String shipName = rs.getString("ShipName");
                String shipAddress = rs.getString("ShipAddress");
                String Status = rs.getString("Status");
                String PaymentMethod = rs.getString("PaymentMethod");

                orders = new Order(orderID, customerID, employeeID, orderDate, requiredDate, shippedDate, freight, shipName, shipAddress, Status, PaymentMethod);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orders;
    }

    public List<OrderCustomerEmployee> getOrderCustomerEmployee() {
        List<OrderCustomerEmployee> orders = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Orders a LEFT JOIN Customers b on a.CustomerID=b.CustomerID LEFT JOIN Employees c on a.EmployeeID=c.EmployeeID order by a.OrderID DESC";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                String customerID = rs.getString("CustomerID");
                int employeeID = rs.getInt("EmployeeID");
                Date orderDate = rs.getDate("OrderDate");
                Date requiredDate = rs.getDate("RequiredDate");
                Date shippedDate = rs.getDate("ShippedDate");
                float freight = rs.getFloat("Freight");
                String shipName = rs.getString("ShipName");
                String shipAddress = rs.getString("ShipAddress");
                String Status = rs.getString("Status");
                String PaymentMethod = rs.getString("PaymentMethod");

                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                int DepartmentID = rs.getInt("DepartmentID");
                String title = rs.getString("Title");
                String titleOfCourtesy = rs.getString("TitleOfCourtesy");
                Date birthDate = rs.getDate("BirthDate");
                Date hireDate = rs.getDate("HireDate");

                String CompanyName = rs.getString("CompanyName");
                String ContactName = rs.getString("ContactName");
                String ContactTitle = rs.getString("ContactTitle");

                Order order = new Order(orderID, customerID, employeeID, orderDate, requiredDate, shippedDate, freight, shipName, shipAddress, Status, PaymentMethod);
                Customers customers = new Customers(customerID, CompanyName, ContactName, ContactTitle);
                Employee employee = new Employee(employeeID, firstName, lastName, DepartmentID, title, titleOfCourtesy, birthDate, hireDate);

                orders.add(new OrderCustomerEmployee(order, customers, employee));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orders;
    }
    public List<OrderCustomerEmployee> getOrderCustomerEmployeeByCompanyNameOrCustomerID(String ID) {
        List<OrderCustomerEmployee> orders = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Orders a LEFT JOIN Customers b on a.CustomerID=b.CustomerID LEFT JOIN Employees c on a.EmployeeID=c.EmployeeID WHERE a.CustomerID LIKE '%'+?+'%' OR b.CompanyName LIKE '%'+?+'%' order by a.OrderID DESC";
            ps = connection.prepareStatement(sql);
            ps.setString(1, ID);
            ps.setString(2, ID);
            rs = ps.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                String customerID = rs.getString("CustomerID");
                int employeeID = rs.getInt("EmployeeID");
                Date orderDate = rs.getDate("OrderDate");
                Date requiredDate = rs.getDate("RequiredDate");
                Date shippedDate = rs.getDate("ShippedDate");
                float freight = rs.getFloat("Freight");
                String shipName = rs.getString("ShipName");
                String shipAddress = rs.getString("ShipAddress");
                String Status = rs.getString("Status");
                String PaymentMethod = rs.getString("PaymentMethod");

                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                int DepartmentID = rs.getInt("DepartmentID");
                String title = rs.getString("Title");
                String titleOfCourtesy = rs.getString("TitleOfCourtesy");
                Date birthDate = rs.getDate("BirthDate");
                Date hireDate = rs.getDate("HireDate");

                String CompanyName = rs.getString("CompanyName");
                String ContactName = rs.getString("ContactName");
                String ContactTitle = rs.getString("ContactTitle");

                Order order = new Order(orderID, customerID, employeeID, orderDate, requiredDate, shippedDate, freight, shipName, shipAddress, Status, PaymentMethod);
                Customers customers = new Customers(customerID, CompanyName, ContactName, ContactTitle);
                Employee employee = new Employee(employeeID, firstName, lastName, DepartmentID, title, titleOfCourtesy, birthDate, hireDate);

                orders.add(new OrderCustomerEmployee(order, customers, employee));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orders;
    }

    public List<OrderCustomerEmployee> getOrderCustomerEmployeeByOrderDate(Date startDate, Date endDate) {
        List<OrderCustomerEmployee> orders = new ArrayList<>();
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT * FROM Orders a LEFT JOIN Customers b on a.CustomerID=b.CustomerID LEFT JOIN Employees c on a.EmployeeID=c.EmployeeID  where a.OrderDate>= ? AND a.OrderDate<= ? order by a.OrderID DESC";
            ps = connection.prepareStatement(sql);
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            rs = ps.executeQuery();
            while (rs.next()) {
                int orderID = rs.getInt("OrderID");
                String customerID = rs.getString("CustomerID");
                int employeeID = rs.getInt("EmployeeID");
                Date orderDate = rs.getDate("OrderDate");
                Date requiredDate = rs.getDate("RequiredDate");
                Date shippedDate = rs.getDate("ShippedDate");
                float freight = rs.getFloat("Freight");
                String shipName = rs.getString("ShipName");
                String shipAddress = rs.getString("ShipAddress");
                String Status = rs.getString("Status");
                String PaymentMethod = rs.getString("PaymentMethod");

                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                int DepartmentID = rs.getInt("DepartmentID");
                String title = rs.getString("Title");
                String titleOfCourtesy = rs.getString("TitleOfCourtesy");
                Date birthDate = rs.getDate("BirthDate");
                Date hireDate = rs.getDate("HireDate");

                String CompanyName = rs.getString("CompanyName");
                String ContactName = rs.getString("ContactName");
                String ContactTitle = rs.getString("ContactTitle");

                Order order = new Order(orderID, customerID, employeeID, orderDate, requiredDate, shippedDate, freight, shipName, shipAddress, Status, PaymentMethod);
                Customers customers = new Customers(customerID, CompanyName, ContactName, ContactTitle);
                Employee employee = new Employee(employeeID, firstName, lastName, DepartmentID, title, titleOfCourtesy, birthDate, hireDate);

                orders.add(new OrderCustomerEmployee(order, customers, employee));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orders;
    }

    public Double getTotalOrder() {
        double totalOrder = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT sum(a.Freight) as Total FROM Orders a";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                totalOrder = rs.getDouble("Total");
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return totalOrder;
    }

    public Double getWeeklySale() {
        double totalOrder = 0;
        try {
            connection = DBContext.getInstance().getConnection();
            String sql = "SELECT sum(a.Freight)\n"
                    + "FROM Orders a \n"
                    + "WHERE a.Status = 'Completed' \n"
                    + "AND a.OrderDate >= dateadd(day, 1-datepart(dw, ?), ?) \n"
                    + "AND a.OrderDate <  dateadd(day, 8-datepart(dw, ?), ?)";
            ps = connection.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            rs = ps.executeQuery();
            while (rs.next()) {
                totalOrder = rs.getDouble(1);
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return totalOrder;
    }

    public double[] getMonthOrderStatistic() {
        HashMap<Integer, Double> orderStatistic = new HashMap<>();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date()); 
        double orderStatisticList[]= new double[cal.get(Calendar.MONTH)+1];
        for(int i=0;i<orderStatisticList.length;i++){
            orderStatisticList[i]=i+1;
        }
        try {
            connection = DBContext.getInstance().getConnection();
            String sql =  "select DATEPART(MM, a.OrderDate), SUM(a.Freight)\n"
                        + "from Orders a \n"
                        + "where DATEPART(YYYY, a.OrderDate) = DATEPART(YYYY, ?) AND a.Status = 'Completed'\n"
                        + "group by DATEPART(MM, a.OrderDate)";
            ps = connection.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            rs = ps.executeQuery();
            while (rs.next()) {
                int month = rs.getInt(1);
                Double orderCOunt = rs.getDouble(2);

                orderStatistic.put(month, orderCOunt);
            } 
            for(int i=0;i<orderStatisticList.length;i++){
                if(orderStatistic.containsKey(i+1)){
                    orderStatisticList[i]=orderStatistic.get(i+1);
                }else{
                    orderStatisticList[i]=0;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomersDao.class.getName()).log(Level.WARNING, e.getMessage(), e);
        } finally {
            DBContext.releaseJBDCObject(rs, ps, connection);
        }
        return orderStatisticList;
    }
}
