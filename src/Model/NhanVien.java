package Model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class NhanVien {
    
    private String maNV;
    private String tenNV;
    private String gioiTinh;
    private String sdt;
    private String chucVu;
    private String email;

    public NhanVien() {
    }

    public NhanVien(String maNV, String tenNV, String gioiTinh, String sdt, String chucVu, String email) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.gioiTinh = gioiTinh;
        this.sdt = sdt;
        this.chucVu = chucVu;
        this.email = email;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getSdt() {
        return sdt;
    }

    public String getChucVu() {
        return chucVu;
    }

    public String getEmail() {
        return email;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}

    

