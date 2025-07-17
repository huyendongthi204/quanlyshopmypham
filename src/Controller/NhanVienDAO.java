package Controller;


import Model.NhanVien;
import Model.connect;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class NhanVienDAO extends connect {

    public NhanVienDAO() {
    }
   

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM nhanvien";
            PreparedStatement ps = getConnecttion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                    rs.getString("MaNV"),
                    rs.getString("HoTen"),
                    rs.getString("GioiTinh"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getString("ChucVu")
                );
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addNhanVien(NhanVien nv) {
        try {
            String sql = "INSERT INTO nhanvien VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps =  getConnecttion().prepareStatement(sql);
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getChucVu());
            ps.setString(6, nv.getEmail());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isMaNhanVienTonTai(String maNV) {
    String sql = "SELECT COUNT(*) FROM nhanvien WHERE MaNV = ?";
    try (PreparedStatement stmt = getConnecttion().prepareStatement(sql)) {
        stmt.setString(1, maNV);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // true nếu đã tồn tại
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

       public boolean insertNhanVien(NhanVien nv) throws SQLException {
        String sql = "INSERT INTO nhanvien (MaNV, HoTen, GioiTinh, SoDienThoai, Email, ChucVu) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = getConnecttion().prepareStatement(sql);
        ps.setString(1, nv.getMaNV());
        ps.setString(2, nv.getTenNV());
        ps.setString(3, nv.getGioiTinh());
        ps.setString(4, nv.getSdt());
        ps.setString(5, nv.getEmail());
        ps.setString(6, nv.getChucVu());
        return ps.executeUpdate() > 0;
    }

    public boolean updateNhanVien(NhanVien nv) throws SQLException {
        String sql = "UPDATE nhanvien SET HoTen=?, GioiTinh=?, SoDienThoai=?, Email=?, ChucVu=? WHERE MaNV=?";
        PreparedStatement ps = getConnecttion().prepareStatement(sql);
        ps.setString(1, nv.getTenNV());
        ps.setString(2, nv.getGioiTinh());
        ps.setString(3, nv.getSdt());
        ps.setString(4, nv.getEmail());
        ps.setString(5, nv.getChucVu());
        ps.setString(6, nv.getMaNV());
        return ps.executeUpdate() > 0;
    }

    public boolean deleteNhanVien(String maNV) throws SQLException {
        String sql = "DELETE FROM nhanvien WHERE MaNV=?";
        PreparedStatement ps = getConnecttion().prepareStatement(sql);
        ps.setString(1, maNV);
        return ps.executeUpdate() > 0;
    }

    // thêm: updateNhanVien(), deleteNhanVien(String ma), findNhanVienByMa()...
   

    
}
