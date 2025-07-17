/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.KhachHang;
import Model.connect;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author PC
 */
public class KhachHangDAO {
     public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM khachhang";
        try (Statement st = connect.getConnecttion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("TenKH"),
                    rs.getString("SDT"),
                    rs.getString("DiaChi")
                );
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
     // Tìm khách hàng theo mã
    public KhachHang getByMaKH(String maKH) {
        String sql = "SELECT * FROM khachhang WHERE MaKH = ?";
        try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("TenKH"),
                    rs.getString("SDT"),
                    rs.getString("DiaChi")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm khách hàng
    public void insert(KhachHang kh) {
        String sql = "INSERT INTO khachhang (MaKH, TenKH, SDT, DiaChi) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setString(4, kh.getDiaChi());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sửa khách hàng
    public void update(KhachHang kh) {
        String sql = "UPDATE khachhang SET TenKH=?, SDT=?, DiaChi=? WHERE MaKH=?";
        try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getDiaChi());
            ps.setString(4, kh.getMaKH());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa khách hàng
    public void delete(String maKH) {
        String sql = "DELETE FROM khachhang WHERE MaKH=?";
        try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
            ps.setString(1, maKH);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
