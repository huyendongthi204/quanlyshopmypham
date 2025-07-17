/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.SanPham;
import Model.connect;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

/**
 *
 * @author PC
 */
public class SanPhamDao {

    public SanPhamDao() {
    }
    
    
       // Lấy tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM sanpham";

        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("MaSP"));
                sp.setTenSP(rs.getString("TenSP"));
                sp.setDonGia(rs.getDouble("DonGia"));
                sp.setSoLuongTon(rs.getInt("SoLuongTon"));
                sp.setDonViTinh(rs.getString("DonViTinh"));
                sp.setHinhAnh(rs.getString("HinhAnh"));

                list.add(sp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm sản phẩm
    public boolean insertSanPham(SanPham sp) {
        String sql = "INSERT INTO sanpham (MaSP, TenSP, DonGia, SoLuongTon, DonViTinh, HinhAnh) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setDouble(3, sp.getDonGia());
            ps.setInt(4, sp.getSoLuongTon());
            ps.setString(5, sp.getDonViTinh());
            ps.setString(6, sp.getHinhAnh());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật sản phẩm
    public boolean updateSanPham(SanPham sp) {
        String sql = "UPDATE sanpham SET TenSP=?, DonGia=?, SoLuongTon=?, DonViTinh=?, HinhAnh=? WHERE MaSP=?";

        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSP());
            ps.setDouble(2, sp.getDonGia());
            ps.setInt(3, sp.getSoLuongTon());
            ps.setString(4, sp.getDonViTinh());
            ps.setString(5, sp.getHinhAnh());
            ps.setString(6, sp.getMaSP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa sản phẩm
    public boolean deleteSanPham(String maSP) {
        String sql = "DELETE FROM sanpham WHERE MaSP=?";

        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra mã sản phẩm đã tồn tại chưa
    public boolean isMaSPTonTai(String maSP) {
        String sql = "SELECT MaSP FROM sanpham WHERE MaSP=?";

        try (Connection conn = connect.getConnecttion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     public double getDonGiaByMaSP(String maSP) {
        double donGia = 0;
        String sql = "SELECT DonGia FROM sanpham WHERE MaSP = ?";
        try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
            ps.setString(1, maSP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                donGia = rs.getDouble("DonGia");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donGia;
    }

    // ✅ Lấy danh sách mã sản phẩm (để đưa vào ComboBox)
    public List<String> getAllMaSP() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaSP FROM sanpham";
        try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("maSP"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public SanPham getByMaSP(String maSP) {
    String sql = "SELECT * FROM sanpham WHERE MaSP = ?";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setString(1, maSP);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new SanPham(
                rs.getString("MaSP"),
                rs.getString("TenSP"),
                rs.getDouble("DonGia"),
                rs.getInt("SoLuongTon"),
                rs.getString("DonViTinh"),
                rs.getString("HinhAnh")
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
}
