package Controller;


import Model.connect;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class DAO extends connect {
    
    public List<String> getDanhSachSanPhamVuotTonKho() throws  SQLException{
    List<String> list = new ArrayList<>();
    String sql = "SELECT TenSP FROM sanpham WHERE SoLuongTon > 50";  // hoặc ngưỡng bạn chọn
    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            list.add(rs.getString("TenSP"));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
public List<String> getSanPhamBanChay() {
    List<String> list = new ArrayList<>();
    String sql = "SELECT sp.TenSP, SUM(ct.SoLuong) AS TongSoLuong " +
                 "FROM chitiethoadon ct " +
                 "JOIN sanpham sp ON ct.MaSP = sp.MaSP " +
                 "GROUP BY ct.MaSP " +
                 "ORDER BY TongSoLuong DESC " +
                 "LIMIT 5";
    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String ten = rs.getString("TenSP");
            int tongSoLuong = rs.getInt("TongSoLuong");
            list.add(ten + " (" + tongSoLuong + ")");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
public List<String> getSanPhamMoi() {
    List<String> list = new ArrayList<>();
    String sql = """
        SELECT sp.TenSP, hdn.NgayLap
        FROM chitiethoadonnhap cthdn
        JOIN sanpham sp ON cthdn.MaSP = sp.MaSP
        JOIN hoadonnhap hdn ON cthdn.MaHDN = hdn.MaHDN
        ORDER BY hdn.NgayLap DESC
        LIMIT 5
        """;

    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String ten = rs.getString("TenSP");
            Date ngayNhap = rs.getDate("NgayLap");
            list.add(ten + " (" + ngayNhap + ")");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

public int demNhaCungCap() {
    String sql = "SELECT COUNT(*) FROM nhacungcap";
    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
public int demSanPhamDangBan() {
    String sql = "SELECT COUNT(*) FROM sanpham WHERE SoLuongTon > 0";
    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
public int demHoaDonHomNay() {
    String sql = "SELECT COUNT(*) FROM hoadon WHERE NgayLap = CURDATE()";
    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}
public int tinhDoanhThuHomNay() {
    String sql = "SELECT SUM(ct.ThanhTien) FROM chitiethoadon ct "
               + "JOIN hoadon hd ON ct.MaHD = hd.MaHD "
               + "WHERE hd.NgayLap = CURDATE()";
    try (Connection conn = connect.getConnecttion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}



    
}
