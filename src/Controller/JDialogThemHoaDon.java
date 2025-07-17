/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ChiTietHoaDon;
import Model.HoaDon;
import Model.KhachHang;
import Model.SanPham;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
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
public class JDialogThemHoaDon extends JDialog {
    private JTextField txtMaHD, txtTongTien;
    private JComboBox<String> cbKhachHang, cbMaSP;
    private JDateChooser ngayLap;
    private JTable tbChiTiet;
    private DefaultTableModel model;
    private JTextField txtSoLuong;
    private JButton btnThemCT, btnXoaCT, btnLuu;

    private SanPhamDao spDAO = new SanPhamDao();
    private HoaDonDAO hdDAO = new HoaDonDAO();
    private ChiTietHoaDonDAO ctDAO = new ChiTietHoaDonDAO();
    private KhachHangDAO khDAO = new KhachHangDAO(); // Nếu bạn có bảng khách hàng

    public JDialogThemHoaDon() {
    }

    
       
    public JDialogThemHoaDon(JFrame parent) {
        super(parent, "Thêm Hóa Đơn Mới", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Panel trên - Nhập thông tin hóa đơn
        JPanel pnlThongTin = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));

        txtMaHD = new JTextField();
        ngayLap = new JDateChooser();
        cbKhachHang = new JComboBox<>();
        txtTongTien = new JTextField("0");
        txtTongTien.setEnabled(false);

        pnlThongTin.add(new JLabel("Mã hóa đơn:"));
        pnlThongTin.add(txtMaHD);
        pnlThongTin.add(new JLabel("Ngày lập:"));
        pnlThongTin.add(ngayLap);
        pnlThongTin.add(new JLabel("Khách hàng:"));
        pnlThongTin.add(cbKhachHang);
        pnlThongTin.add(new JLabel("Tổng tiền:"));
        pnlThongTin.add(txtTongTien);

        // Panel giữa - Chi tiết sản phẩm
        JPanel pnlChiTiet = new JPanel(new BorderLayout());
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));

        model = new DefaultTableModel(new String[]{"Mã SP", "Số lượng", "Đơn giá", "Thành tiền"}, 0);
        tbChiTiet = new JTable(model);
        JScrollPane scroll = new JScrollPane(tbChiTiet);

        JPanel pnlThem = new JPanel();
        cbMaSP = new JComboBox<>();
        txtSoLuong = new JTextField(5);
        btnThemCT = new JButton("Thêm sản phẩm");
        btnXoaCT = new JButton("Xóa dòng");

        pnlThem.add(new JLabel("Mã SP:"));
        pnlThem.add(cbMaSP);
        pnlThem.add(new JLabel("Số lượng:"));
        pnlThem.add(txtSoLuong);
        pnlThem.add(btnThemCT);
        pnlThem.add(btnXoaCT);

        pnlChiTiet.add(pnlThem, BorderLayout.NORTH);
        pnlChiTiet.add(scroll, BorderLayout.CENTER);

        // Panel dưới - nút lưu
        JPanel pnlButton = new JPanel();
        btnLuu = new JButton("Lưu hóa đơn");
        pnlButton.add(btnLuu);

        add(pnlThongTin, BorderLayout.NORTH);
        add(pnlChiTiet, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);
       
        
        loadKhachHang();
        loadSanPham();

        addActionListeners();
    }
    private void loadSanPham() {
    List<SanPham> list = spDAO. getAllSanPham();
    cbMaSP.removeAllItems();
    for (SanPham sp : list) {
        cbMaSP.addItem(sp.getMaSP()); // chỉ mã SP, bạn có thể thay bằng "MaSP - TenSP"
    }
}
    private void loadKhachHang() {
    List<KhachHang> list = khDAO.getAll();
    cbKhachHang.removeAllItems();
    
    // Thêm lựa chọn trống/null đầu tiên
    cbKhachHang.addItem(""); // hoặc "Không có"
    for (KhachHang kh : list) {
        cbKhachHang.addItem(kh.getMaKH()); // hoặc cbKhachHang.addItem(kh.toString());
    }
}
    private void themChiTiet() {
    String maSP = (String) cbMaSP.getSelectedItem();
    int soLuong;

    try {
        soLuong = Integer.parseInt(txtSoLuong.getText());
        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ");
        return;
    }

    SanPham sp = spDAO.getByMaSP(maSP);
    if (sp == null) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
        return;
    }

    double thanhTien = sp.getDonGia() * soLuong;
    model.addRow(new Object[]{maSP, soLuong, sp.getDonGia(), thanhTien});
    capNhatTongTien();
}

    private void capNhatTongTien() {
    double tongTien = 0;
    for (int i = 0; i < model.getRowCount(); i++) {
        tongTien += (double) model.getValueAt(i, 3); // thành tiền
    }
    txtTongTien.setText(String.valueOf(tongTien));
}
private void xoaChiTiet() {
    int selectedRow = tbChiTiet.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa");
        return;
    }
    model.removeRow(selectedRow);
    capNhatTongTien();
}
private void luuHoaDon() {
    String maHD = txtMaHD.getText().trim();
    if (maHD.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập mã hóa đơn");
        return;
    }

    if (hdDAO.getHoaDonByMa(maHD) != null) {
        JOptionPane.showMessageDialog(this, "Mã hóa đơn đã tồn tại!");
        return;
    }

   String maKH = (String) cbKhachHang.getSelectedItem();
if (maKH != null && maKH.trim().isEmpty()) {
    maKH = null; // Nếu chọn dòng trống ("") thì gán null để lưu xuống DB
}

    Date ngay = ngayLap.getDate();
    if (ngay == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày lập");
        return;
    }

   BigDecimal tongTien = new BigDecimal(txtTongTien.getText());


    // Thêm hóa đơn
    HoaDon hd = new HoaDon(maHD, new java.sql.Date(ngay.getTime()), maKH, tongTien);
    hdDAO.insertHoaDon(hd);

    // Thêm chi tiết
    for (int i = 0; i < model.getRowCount(); i++) {
        String maSP = (String) model.getValueAt(i, 0);
        int soLuong = (int) model.getValueAt(i, 1);
        double thanhTien = (double) model.getValueAt(i, 3);
        ctDAO.insert(new ChiTietHoaDon(maHD, maSP, soLuong, thanhTien));
    }

    JOptionPane.showMessageDialog(this, "Thêm hóa đơn thành công!");
    this.dispose();
}
private void addActionListeners() {
    btnThemCT.addActionListener(e -> themChiTiet());
    btnXoaCT.addActionListener(e -> xoaChiTiet());
    btnLuu.addActionListener(e -> luuHoaDon());
}
}