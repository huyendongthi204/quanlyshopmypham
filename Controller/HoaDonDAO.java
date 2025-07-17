/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.HoaDon;
import Model.connect;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import javax.swing.JOptionPane;


/**
 *
 * @author PC
 */
public class HoaDonDAO {
     public List<HoaDon> getAllHoaDon() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon";
        try (Connection con = connect.getConnecttion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getString("MaHD"));
                hd.setNgayLap(rs.getDate("NgayLap"));
                hd.setMaKH(rs.getString("MaKH"));
                hd.setTongTien(rs.getBigDecimal("TongTien"));
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertHoaDon(HoaDon hd) {
       String sql = "INSERT INTO hoadon (MaHD, MaKH, NgayLap, TongTien) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setString(1, hd.getMaHD());

        if (hd.getMaKH() == null || hd.getMaKH().isEmpty()) {
            ps.setNull(2, java.sql.Types.VARCHAR);
        } else {
            ps.setString(2, hd.getMaKH());
        }

        ps.setDate(3, new java.sql.Date(hd.getNgayLap().getTime()));
        ps.setBigDecimal(4, hd.getTongTien());

        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
         return false;
    }

  public void updateHoaDon(HoaDon hd, String maKH) {
    String sql = "UPDATE hoadon SET NgayLap = ?, MaKH = ?, TongTien = ? WHERE MaHD = ?";
    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setDate(1, new java.sql.Date(hd.getNgayLap().getTime()));
        System.out.println("maKH = " + (maKH == null ? "null" : "'" + maKH + "'"));

        if (maKH == null || maKH.trim().isEmpty()) {
     ps.setNull(2, java.sql.Types.VARCHAR);
        } else {
    ps.setString(2, maKH);
}


        ps.setBigDecimal(3, hd.getTongTien());
        ps.setString(4, hd.getMaHD());

        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public boolean deleteHoaDon(String maHD) {
        String sql = "DELETE FROM hoadon WHERE MaHD=?";
        try (Connection con = connect.getConnecttion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void updateTongTien(String maHD, double tongTien) {
    String sql = "UPDATE hoadon SET TongTien = ? WHERE maHD = ?";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setDouble(1, tongTien);
        ps.setString(2, maHD);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public HoaDon getHoaDonByMa(String maHD) {
    String sql = "SELECT * FROM hoadon WHERE MaHD = ?";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setString(1, maHD);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new HoaDon(
                rs.getString("MaHD"),
                rs.getDate("NgayLap"),
                rs.getString("MaKH"),
                rs.getBigDecimal("TongTien")
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    public void updateMaHD(String oldMaHD, String newMaHD) {
    String sql = "UPDATE hoadon SET MaHD = ? WHERE MaHD = ?";
    try (PreparedStatement ps = connect.getConnecttion().prepareStatement(sql)) {
        ps.setString(1, newMaHD);
        ps.setString(2, oldMaHD);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
