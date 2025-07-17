/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author PC
 */
public class SanPham {
     private String maSP;
    private String tenSP;
    private double donGia;
    private int soLuongTon;
    private String donViTinh;
    private String hinhAnh;

    // Constructor đầy đủ
    public SanPham(String maSP, String tenSP, double donGia, int soLuongTon, String donViTinh, String hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
        this.donViTinh = donViTinh;
        this.hinhAnh = hinhAnh;
    }

    // Constructor rỗng
    public SanPham() {}

    // Getter và Setter
    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    

    @Override
    public String toString() {
        return tenSP; // dùng khi hiển thị trong combobox hoặc log
    }
    
}
