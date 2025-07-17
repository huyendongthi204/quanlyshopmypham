package Model;



import java.sql.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class connect {
    public static Connection getConnecttion() throws SQLException
    {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/qlmypham?useUnicode=true&characterEncoding=UTF-8","root","");
    }
   
}
