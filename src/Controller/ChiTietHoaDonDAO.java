/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ChiTietHoaDon;
import Model.connect;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author PC
 */
public class ChiTietHoaDonDAO {
      public static List<ChiTietHoaDon> getChiTietByMaHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM chitiethoadon WHERE MaHD = ?";
        
        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setMaHD(rs.getString("MaHD"));
                ct.setMaSP(rs.getString("MaSP"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                
                list.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insertChiTiet(ChiTietHoaDon ct) {
        String sql = "INSERT INTO chitiethoadon (MaHD, MaSP, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, ct.getMaHD());
            ps.setString(2, ct.getMaSP());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getThanhTien());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean deleteChiTietByMaHD(String maHD) {
        String sql = "DELETE FROM chitiethoadon WHERE MaHD = ?";
        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void insert(ChiTietHoaDon ct) {
    String sql = "INSERT INTO chitiethoadon(maHD, maSP, SoLuong, ThanhTien) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setString(1, ct.getMaHD());
        ps.setString(2, ct.getMaSP());
        ps.setInt(3, ct.getSoLuong());
        ps.setDouble(4, ct.getThanhTien());
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void update(ChiTietHoaDon ct) {
    String sql = "UPDATE chitiethoadon SET SoLuong = ?, ThanhTien = ? WHERE MaHD = ? AND MaSP = ?";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setInt(1, ct.getSoLuong());
        ps.setDouble(2, ct.getThanhTien());
        ps.setString(3, ct.getMaHD());
        ps.setString(4, ct.getMaSP());
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void delete(String maHD, String maSP) {
    String sql = "DELETE FROM chitiethoadon WHERE MaHD = ? AND MaSP = ?";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setString(1, maHD);
        ps.setString(2, maSP);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
public void updateMaHDCuaChiTiet(String oldMaHD, String newMaHD) {
    String sql = "UPDATE chitiethoadon SET MaHD = ? WHERE MaHD = ?";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setString(1, newMaHD);
        ps.setString(2, oldMaHD);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
  public List<Object[]> getAllChiTietWithDonGia() {
    List<Object[]> list = new ArrayList<>();
    String sql = "SELECT ct.MaHD, ct.MaSP, ct.SoLuong, sp.DonGia, ct.SoLuong * sp.DonGia AS ThanhTien " +
                 "FROM chitiethoadon ct JOIN sanpham sp ON ct.MaSP = sp.MaSP";
    try (
        Connection con = connect.getConnecttion();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {
        while (rs.next()) {
            String maHD = rs.getString("MaHD");
            String maSP = rs.getString("MaSP");
            int soLuong = rs.getInt("SoLuong");
            double donGia = rs.getDouble("DonGia");
            double thanhTien = rs.getDouble("ThanhTien");

            Object[] row = { maHD, maSP, soLuong, donGia, thanhTien };
            list.add(row);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}  
}
