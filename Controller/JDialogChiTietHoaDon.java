/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ChiTietHoaDon;
import View.quanlybanhang;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class JDialogChiTietHoaDon extends JDialog {
   
    private JTable tbChiTiet;
    private DefaultTableModel model;
    private ChiTietHoaDonDAO ctDao = new ChiTietHoaDonDAO();
    private SanPhamDao spDao = new SanPhamDao();
    private HoaDonDAO hdDao = new HoaDonDAO(); // để cập nhật tổng tiền
    private String maHD;

    public JDialogChiTietHoaDon(JFrame parent, String maHD) {
        super(parent, "Chi Tiết Hóa Đơn: " + maHD, true);
        this.maHD = maHD;

        setSize(750, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
       


        // Bảng
       model = new DefaultTableModel(new String[]{"Mã HĐ","Mã SP", "Số lượng", "Thành tiền"}, 0);
       tbChiTiet = new JTable(model);
        tbChiTiet.getTableHeader().setFont(new java.awt.Font("Segoe UI", Font.BOLD, 15)); // ✅ OK

JScrollPane scroll = new JScrollPane(tbChiTiet);
add(scroll, BorderLayout.CENTER);

        // Nút thao tác
        JPanel buttonPanel = new JPanel();

        JButton btnThem = new JButton("Thêm");
        btnThem.addActionListener(e -> themChiTiet());

        JButton btnSua = new JButton("Sửa");
        btnSua.addActionListener(e -> suaChiTiet());

        JButton btnXoa = new JButton("Xóa");
        btnXoa.addActionListener(e -> xoaChiTiet());

        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnDong);
        add(buttonPanel, BorderLayout.SOUTH);

        hienThiChiTiet(maHD);
    }

    private void hienThiChiTiet(String maHD) {
          model.setRowCount(0);
    List<ChiTietHoaDon> list = ctDao.getChiTietByMaHD(maHD);
    for (ChiTietHoaDon ct : list) {
        model.addRow(new Object[]{
            ct.getMaHD(), ct.getMaSP(), ct.getSoLuong(), ct.getThanhTien()
        });
    }
    }

    private void themChiTiet() {
        // Tạo combo chọn mã SP
        JComboBox<String> cboMaSP = new JComboBox<>();
        for (String maSP : spDao.getAllMaSP()) {
            cboMaSP.addItem(maSP);
        }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Mã sản phẩm:"));
        panel.add(cboMaSP);
        panel.add(new JLabel("Số lượng:"));
        JTextField txtSoLuong = new JTextField();
        panel.add(txtSoLuong);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Chi Tiết", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String maSP = (String) cboMaSP.getSelectedItem();
            int soLuong;
            try {
                soLuong = Integer.parseInt(txtSoLuong.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
                return;
            }

            double donGia = spDao.getDonGiaByMaSP(maSP);
            double thanhTien = donGia * soLuong;

            ChiTietHoaDon ct = new ChiTietHoaDon(maHD, maSP, soLuong, thanhTien);
            ctDao.insert(ct);

            capNhatTongTien();
            hienThiChiTiet(maHD);
        }
    }

    private void xoaChiTiet() {
        int row = tbChiTiet.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn dòng để xóa!");
            return;
        }

        String maSP = model.getValueAt(row, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa SP " + maSP + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ctDao.delete(maHD, maSP);
            capNhatTongTien();
            hienThiChiTiet(maHD);
        }
    }

    private void suaChiTiet() {
         int row = tbChiTiet.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Chọn dòng để sửa!");
        return;
    }

    String maSP = model.getValueAt(row, 1).toString(); // Nếu cột mã SP là cột 1
    String slStr = JOptionPane.showInputDialog(this, "Nhập số lượng mới:");

    if (slStr == null || slStr.isEmpty()) return;

    try {
        int soLuong = Integer.parseInt(slStr);
        double donGia = spDao.getDonGiaByMaSP(maSP);
        double thanhTien = soLuong * donGia;

        ChiTietHoaDon ct = new ChiTietHoaDon(maHD, maSP, soLuong, thanhTien);
        ctDao.update(ct);
        capNhatTongTien();
        hienThiChiTiet(maHD);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
    }
    }

    private void capNhatTongTien() {
        double tong = 0;
        List<ChiTietHoaDon> list = ctDao.getChiTietByMaHD(maHD);
        for (ChiTietHoaDon ct : list) {
            tong += ct.getThanhTien();
        }
        hdDao.updateTongTien(maHD, tong); // cập nhật bảng hoadon
    }


}