package com.uofm.corenlp;

//STEP 1. Import required packages
import java.sql.*;
import java.util.Scanner;

public class Updation {
 // JDBC driver name and database URL
 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
 static final String DB_URL = "jdbc:mysql://localhost/test";

 //  Database credentials
 static final String USER = "root";
 static final String PASS = "moosa";
 private static Scanner sc;

 public static void main(String[] args) {
 Connection conn = null;
 Statement stmt = null;
 try{
    //STEP 2: Register JDBC driver
    Class.forName("com.mysql.cj.jdbc.Driver");
    //STEP 3: Open a connection
    System.out.println("Connecting to database...");
    conn = DriverManager.getConnection(DB_URL,USER,PASS);
    //STEP 4: Execute a query
    System.out.println("Creating statement...");
    stmt = conn.createStatement();
    String sql;
    //STEP 5: Get the employee_id for whom data need to be updated/inserted
    sc = new Scanner(System.in);
    System.out.println("Enter the Employee_id for the record to be updated or inserted");
    int Emp_idvalue=sc.nextInt();
    sql = "SELECT * FROM EmployeeDetails where Emp_id=" + Emp_idvalue;
    ResultSet rs = stmt.executeQuery(sql);
    if (!rs.next())
    {
        //STEP 6: If the previous details is not there ,then the details will be inserted newly
        System.out.println("Enter the name to be inserted");
        String Emp_namevalue =sc.next();
        System.out.println("Enter the address to be inserted");
        String Emp_addvalue =sc.next();
        System.out.println("Enter the role to be inserted");
        String Emp_rolevalue =sc.next();
        PreparedStatement ps = conn
                  .prepareStatement("insert into EmployeeDetails values(?,?,?,?)");
          ps.setString(2, Emp_namevalue);
          ps.setString(3, Emp_addvalue);
          ps.setString(4, Emp_rolevalue);
          ps.setInt(1, Emp_idvalue);
          ps.executeUpdate();
          System.out.println("Inserted successfully");
    }
    else
    {
      //STEP 7: If the previous details is  there ,then the details will be updated 
        System.out.println("Enter the name to be updated");
        String Emp_namevalue =sc.next();
        System.out.println("Enter the address to be updated");
        String Emp_addvalue =sc.next();
        System.out.println("Enter the role to be updated");
        String Emp_rolevalue =sc.next();
        String updateQuery = "update EmployeeDetails set Emp_id=?,Emp_name=?, Emp_address=?, Emp_role=? where Emp_id='"
                  + Emp_idvalue + "'";
          PreparedStatement ps1 = conn.prepareStatement(updateQuery);
          ps1.setString(2, Emp_namevalue);
          ps1.setString(3, Emp_addvalue);
          ps1.setString(4, Emp_rolevalue);
          ps1.setInt(1, Emp_idvalue);
          ps1.executeUpdate();    
          System.out.println("updated successfully");

    }
    //Clean-up environment
    rs.close();
    stmt.close();
    conn.close();
 }catch(SQLException se){
    //Handle errors for JDBC
    se.printStackTrace();

 }catch(Exception e){
    //Handle errors for Class.forName
    e.printStackTrace();
}
}
}
