package Controller;


import View.FromAdmin;
import View.DangNhapUI;
import com.formdev.flatlaf.FlatLightLaf;
import java.sql.SQLException;
import javax.swing.UIManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class Main {
     public static void main(String[] args) throws SQLException {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new DangNhapUI().setVisible(true); // hoặc TrangChuUI
        new FromAdmin().setVisible(true);
    }
}
