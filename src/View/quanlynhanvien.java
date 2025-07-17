package View;


import Controller.panelvien;
import Controller.RoundedTextField;
import Model.connect;
import Model.NhanVien;
import Controller.NhanVienDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JTextField;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

/**
 *
 * @author PC
 */
public class quanlynhanvien extends javax.swing.JPanel {

    /**
     * Creates new form quanlynhanvien
     */
    public quanlynhanvien() throws SQLException {
        initComponents();
        tbnhanvien.getTableHeader().setBackground(new java.awt.Color(255, 228, 214)); // 🍑 hồng cam nhạt
        tbnhanvien.getTableHeader().setForeground(new java.awt.Color(0, 0, 0));       // 🖤 chữ đen

        tbnhanvien.getTableHeader().setFont(new java.awt.Font("Segoe UI", Font.BOLD, 15));
            setPlaceholder(txtma, "Mã NV");
            setPlaceholder(txt_tennhanvien, "Họ tên");
            setPlaceholder(txt_sdt, "SĐT");
            setPlaceholder(txt_email, "Email");
            setPlaceholder(txt_chucvu, "Chức vụ");
            setPlaceholder(txtgioitinh, "Giới tính");
            setPlaceholder(txt_timkiem, "Nhập từ khóa tìm kiếm (tên, mã...)");
            loadTableNhanVien();
            canGiuaTable();
     tbnhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = tbnhanvien.getSelectedRow();
        if (selectedRow >= 0) {
            hienThongTinLenTextField(selectedRow);
        }
    }
});
     txt_timkiem.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) {
        locNhanVienTheoTuKhoa();
    }

    public void removeUpdate(DocumentEvent e) {
        locNhanVienTheoTuKhoa();
    }

    public void changedUpdate(DocumentEvent e) {
        locNhanVienTheoTuKhoa();
    }
});



}
    private void hienThongTinLenTextField(int row) {
    txtma.setText(tbnhanvien.getValueAt(row, 0).toString());
    txt_tennhanvien.setText(tbnhanvien.getValueAt(row, 1).toString());
    txtgioitinh.setText(tbnhanvien.getValueAt(row, 2).toString());
    txt_sdt.setText(tbnhanvien.getValueAt(row, 3).toString());
    txt_email.setText(tbnhanvien.getValueAt(row, 4).toString());
    txt_chucvu.setText(tbnhanvien.getValueAt(row, 5).toString());
}

    private void xoaNhanVien() throws SQLException {
    String maNV = txtma.getText().trim();

    if (maNV.isEmpty() || maNV.equals("Mã NV")) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên có mã: " + maNV + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        NhanVienDAO dao = new NhanVienDAO();
        if (dao.deleteNhanVien(maNV)) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadTableNhanVien();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại.");
        }
    }
}

 public void setPlaceholder(JTextField field, String placeholder) throws SQLException {
    field.setText(placeholder);
    field.setForeground(Color.GRAY);

    field.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(Color.BLACK);
            }
        }

        public void focusLost(FocusEvent e) {
            if (field.getText().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(Color.GRAY);
            }
        }
    });
      
    }
     
 public void loadTableNhanVien() throws SQLException {
    // Khởi tạo DAO
    Connection conn = connect.getConnecttion(); // DBConnect là class chứa getConnection()
    NhanVienDAO dao = new NhanVienDAO();
    
    // Lấy dữ liệu
    List<NhanVien> list = dao.getAllNhanVien();
    
    // Đổ lên JTable
    DefaultTableModel model = (DefaultTableModel) tbnhanvien.getModel();
    model.setRowCount(0); // Xóa dòng cũ nếu có
    
    for (NhanVien nv : list) {
        model.addRow(new Object[]{
            nv.getMaNV(),
            nv.getTenNV(),
            nv.getGioiTinh(),
            nv.getSdt(),
            nv.getChucVu(),
            nv.getEmail()
        });
    }
}
 public void canGiuaTable() {
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.CENTER);
    tbnhanvien.setShowGrid(true); // Hiển thị lưới
    tbnhanvien.setGridColor(Color.GRAY); // Màu viền

    renderer.setForeground(new Color(0, 0, 0)); // 🎨 Màu chữ hồng đậm

for (int i = 0; i < tbnhanvien.getColumnCount(); i++) {
    tbnhanvien.getColumnModel().getColumn(i).setCellRenderer(renderer);
}

}
 private void clearForm() {
    txtma.setText("Mã NV");
    txtma.setForeground(Color.GRAY);

    txt_tennhanvien.setText("Tên Nhân Viên");
    txt_tennhanvien.setForeground(Color.GRAY);

    txtgioitinh.setText("Giới Tính");
    txtgioitinh.setForeground(Color.GRAY);

    txt_sdt.setText("Số Điện Thoại");
    txt_sdt.setForeground(Color.GRAY);

    txt_email.setText("Email");
    txt_email.setForeground(Color.GRAY);

    txt_chucvu.setText("Chức Vụ");
    txt_chucvu.setForeground(Color.GRAY);
}

 public void themNhanVienTuPopup(JDialog dialog,
                                JTextField tfMa,
                                JTextField tfTen,
                                JTextField tfGioiTinh,
                                JTextField tfSDT,
                                JTextField tfEmail,
                                JTextField tfChucVu) {

    String ma = tfMa.getText().trim();
    String ten = tfTen.getText().trim();
    String gioiTinh = tfGioiTinh.getText().trim();
    String sdt = tfSDT.getText().trim();
    String email = tfEmail.getText().trim();
    String chucVu = tfChucVu.getText().trim();

    // Kiểm tra rỗng
    if (ma.isEmpty() || ten.isEmpty() || gioiTinh.isEmpty() ||
        sdt.isEmpty() || email.isEmpty() || chucVu.isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    NhanVienDAO dao = new NhanVienDAO();

    try {
        if (dao.isMaNhanVienTonTai(ma)) {
            JOptionPane.showMessageDialog(dialog, "Mã nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NhanVien nv = new NhanVien();
        nv.setMaNV(ma);
        nv.setTenNV(ten);
        nv.setGioiTinh(gioiTinh);
        nv.setSdt(sdt);
        nv.setEmail(email);
        nv.setChucVu(chucVu);

        if (dao.insertNhanVien(nv)) {
            JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadTableNhanVien(); // load lại bảng
        } else {
            JOptionPane.showMessageDialog(dialog, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(dialog, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

 private void capNhatNhanVien() throws SQLException {
    String maNV = txtma.getText().trim();

    // Kiểm tra nhập liệu
    if (maNV.isEmpty() || maNV.equals("Mã NV") ||
        txt_tennhanvien.getText().trim().isEmpty() ||
        txtgioitinh.getText().trim().isEmpty() ||
        txt_sdt.getText().trim().isEmpty() ||
        txt_email.getText().trim().isEmpty() ||
        txt_chucvu.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Xác nhận người dùng
    int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn cập nhật nhân viên này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (choice != JOptionPane.YES_OPTION) return;

    // Tạo đối tượng nhân viên
    NhanVien nv = new NhanVien();
    nv.setMaNV(maNV);
    nv.setTenNV(txt_tennhanvien.getText().trim());
    nv.setGioiTinh(txtgioitinh.getText().trim());
    nv.setSdt(txt_sdt.getText().trim());
    nv.setEmail(txt_email.getText().trim());
    nv.setChucVu(txt_chucvu.getText().trim());

    // Gọi DAO để cập nhật
    NhanVienDAO dao = new NhanVienDAO();
    if (dao.updateNhanVien(nv)) {
        JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        loadTableNhanVien(); // refresh bảng
        clearForm();         // xóa form
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật thất bại.");
    }
}
 


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelquanlynhanvien = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtma = new RoundedTextField(20)
        ;
        txt_sdt = new RoundedTextField(20)
        ;
        jLabel4 = new javax.swing.JLabel();
        txtgioitinh = new RoundedTextField(20)
        ;
        jLabel5 = new javax.swing.JLabel();
        txt_tennhanvien = new RoundedTextField(20)
        ;
        jLabel6 = new javax.swing.JLabel();
        txt_chucvu = new RoundedTextField(20)
        ;
        jLabel7 = new javax.swing.JLabel();
        txt_email = new RoundedTextField(20)
        ;
        bt_them = new javax.swing.JButton();
        bt_xoa = new javax.swing.JButton();
        bt_capnhat = new javax.swing.JButton();
        bt_xuat = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbnhanvien = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_timkiem = new javax.swing.JTextField();

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelquanlynhanvien.setBackground(new java.awt.Color(255, 230, 242));
        panelquanlynhanvien.setRequestFocusEnabled(false);
        panelquanlynhanvien.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Mã Nhân Viên ");

        jLabel3.setText("Tên Nhân Viên");

        txtma.setForeground(new java.awt.Color(204, 204, 204));

        txt_sdt.setForeground(new java.awt.Color(204, 204, 204));

        jLabel4.setText("Giới Tính");

        txtgioitinh.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        txtgioitinh.setForeground(new java.awt.Color(204, 204, 204));

        jLabel5.setText("Số Điện Thoại ");

        txt_tennhanvien.setForeground(new java.awt.Color(204, 204, 204));

        jLabel6.setText("Chức Vụ ");

        txt_chucvu.setForeground(new java.awt.Color(153, 153, 153));

        jLabel7.setText("Email ");

        txt_email.setForeground(new java.awt.Color(153, 153, 153));

        bt_them.setBackground(new java.awt.Color(255, 102, 153));
        bt_them.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_them.setForeground(new java.awt.Color(255, 255, 255));
        bt_them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-plus-48.png"))); // NOI18N
        bt_them.setText("Thêm ");
        bt_them.setIconTextGap(20);
        bt_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_themActionPerformed(evt);
            }
        });

        bt_xoa.setBackground(new java.awt.Color(204, 204, 255));
        bt_xoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-delete-48.png"))); // NOI18N
        bt_xoa.setText("Xóa ");
        bt_xoa.setIconTextGap(20);
        bt_xoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_xoaActionPerformed(evt);
            }
        });

        bt_capnhat.setBackground(new java.awt.Color(0, 153, 153));
        bt_capnhat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_capnhat.setForeground(new java.awt.Color(255, 255, 255));
        bt_capnhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-update-48.png"))); // NOI18N
        bt_capnhat.setText("Cập nhật ");
        bt_capnhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_capnhatActionPerformed(evt);
            }
        });

        bt_xuat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_xuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-export-50.png"))); // NOI18N
        bt_xuat.setText("Export");
        bt_xuat.setIconTextGap(20);
        bt_xuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_xuatActionPerformed(evt);
            }
        });

        tbnhanvien.setForeground(new java.awt.Color(255, 153, 204));
        tbnhanvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã Nhân Viên", "Họ Tên ", "Giơi Tính ", "Số Điện Thoại ", "Email ", "Chức vụ "
            }
        ));
        jScrollPane2.setViewportView(tbnhanvien);
        if (tbnhanvien.getColumnModel().getColumnCount() > 0) {
            tbnhanvien.getColumnModel().getColumn(0).setHeaderValue("Mã Nhân Viên");
            tbnhanvien.getColumnModel().getColumn(1).setHeaderValue("Họ Tên ");
            tbnhanvien.getColumnModel().getColumn(2).setHeaderValue("Giơi Tính ");
            tbnhanvien.getColumnModel().getColumn(3).setHeaderValue("Số Điện Thoại ");
            tbnhanvien.getColumnModel().getColumn(4).setHeaderValue("Email ");
            tbnhanvien.getColumnModel().getColumn(5).setHeaderValue("Chức vụ ");
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtma, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtgioitinh, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                            .addComponent(txt_tennhanvien))))
                                .addGap(43, 43, 43)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(bt_capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(bt_them, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(bt_xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_sdt)
                            .addComponent(txt_chucvu)
                            .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(bt_xuat)
                        .addGap(188, 188, 188))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_tennhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txt_chucvu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtgioitinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_them, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_xuat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(212, 45, 126));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icons8-user-64.png"))); // NOI18N
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN");

        jLabel8.setText("Tìm Kiếm Nhân Viên");

        txt_timkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_timkiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addGap(198, 198, 198)
                            .addComponent(jLabel8)
                            .addGap(26, 26, 26)
                            .addComponent(txt_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_timkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelquanlynhanvien.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 20, 910, 500));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelquanlynhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(422, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelquanlynhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bt_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_themActionPerformed
        // TODO add your handling code here:
      
    JDialog dialog = new JDialog((JFrame) null, "Thêm Nhân Viên", true);
    dialog.setSize(500, 380);
    dialog.setLocationRelativeTo(null);
    dialog.setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 5, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lblTitle = new JLabel("➕ Thêm Nhân Viên");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitle.setForeground(new Color(204, 0, 102)); // màu hồng đậm

    dialog.add(lblTitle, BorderLayout.NORTH);

    JTextField tfMa = new JTextField();
    JTextField tfTen = new JTextField();
    JTextField tfGioiTinh = new JTextField();
    JTextField tfSDT = new JTextField();
    JTextField tfEmail = new JTextField();
    JTextField tfChucVu = new JTextField();

    // Hàng 1
    gbc.gridx = 0; gbc.gridy = 0;
    contentPanel.add(new JLabel("Mã NV:"), gbc);
    gbc.gridx = 1;
    contentPanel.add(tfMa, gbc);

    gbc.gridx = 2;
    contentPanel.add(new JLabel("Tên NV:"), gbc);
    gbc.gridx = 3;
    contentPanel.add(tfTen, gbc);

    // Hàng 2
    gbc.gridx = 0; gbc.gridy = 1;
    contentPanel.add(new JLabel("Giới Tính:"), gbc);
    gbc.gridx = 1;
    contentPanel.add(tfGioiTinh, gbc);

    gbc.gridx = 2;
    contentPanel.add(new JLabel("SĐT:"), gbc);
    gbc.gridx = 3;
    contentPanel.add(tfSDT, gbc);

    // Hàng 3
    gbc.gridx = 0; gbc.gridy = 2;
    contentPanel.add(new JLabel("Email:"), gbc);
    gbc.gridx = 1;
    contentPanel.add(tfEmail, gbc);

    gbc.gridx = 2;
    contentPanel.add(new JLabel("Chức Vụ:"), gbc);
    gbc.gridx = 3;
    contentPanel.add(tfChucVu, gbc);

    // Nút Lưu
    JButton btnLuu = new JButton("Lưu");
    btnLuu.setBackground(new Color(204, 0, 102));
    btnLuu.setForeground(Color.WHITE);
    btnLuu.setFocusPainted(false);
    btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));

    gbc.gridx = 0; gbc.gridy = 3;
    gbc.gridwidth = 4;
    gbc.anchor = GridBagConstraints.CENTER;
    contentPanel.add(btnLuu, gbc);

    btnLuu.addActionListener(ev -> {
        
        String ma = tfMa.getText();
        String ten = tfTen.getText();
        String gioiTinh = tfGioiTinh.getText();
        String sdt = tfSDT.getText();
        String email = tfEmail.getText();
        String chucVu = tfChucVu.getText();
         
      themNhanVienTuPopup(dialog, tfMa, tfTen, tfGioiTinh, tfSDT, tfEmail, tfChucVu); // 👉 Gọi hàm xử lý thêm đã viết ở trên
        dialog.dispose(); // đóng form nếu thêm thành công
   
        // Kiểm tra và xử lý
        System.out.println("Thêm NV: " + ten);
        dialog.dispose();
    });

    dialog.add(contentPanel, BorderLayout.CENTER);
    dialog.setVisible(true);

//        bt_them.addActionListener(e -> {
//            try {
//                themNhanVien();
//            } catch (SQLException ex) {
//                Logger.getLogger(quanlynhanvien.class.getName()).log(Level.SEVERE, null, ex);
//            }
       // });


    }//GEN-LAST:event_bt_themActionPerformed

    private void bt_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_xoaActionPerformed
        // TODO add your handling code here:
        bt_xoa.addActionListener(e -> {
            try {
                xoaNhanVien();
            } catch (SQLException ex) {
                Logger.getLogger(quanlynhanvien.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }//GEN-LAST:event_bt_xoaActionPerformed

    private void bt_capnhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_capnhatActionPerformed
        try {
            // TODO add your handling code here:
            capNhatNhanVien();
        } catch (SQLException ex) {
            Logger.getLogger(quanlynhanvien.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bt_capnhatActionPerformed

    private void txt_timkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timkiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timkiemActionPerformed

    private void bt_xuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_xuatActionPerformed
        // TODO add your handling code here:
        bt_xuat.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        fileChooser.setSelectedFile(new File("danhsach_nhanvien.xlsx"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            exportToExcel(tbnhanvien, fileToSave);
        }
    }
});

    }//GEN-LAST:event_bt_xuatActionPerformed
private void locNhanVienTheoTuKhoa() {
    String keyword = txt_timkiem.getText().trim().toLowerCase();

    DefaultTableModel model = (DefaultTableModel) tbnhanvien.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    tbnhanvien.setRowSorter(sorter);

    if (keyword.isEmpty()) {
        sorter.setRowFilter(null); // Hiện tất cả nếu không nhập gì
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
    }
}
private void exportToExcel(JTable table, File file) {
    try {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Nhân viên");

        // Header
        XSSFRow headerRow = sheet.createRow(0);
        TableModel model = table.getModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
            headerRow.createCell(i).setCellValue(model.getColumnName(i));
        }

        // Dữ liệu
        for (int i = 0; i < model.getRowCount(); i++) {
            XSSFRow row = sheet.createRow(i + 1);
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object value = model.getValueAt(i, j);
                row.createCell(j).setCellValue(value != null ? value.toString() : "");
            }
        }

        // Ghi ra file
        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);
        out.close();
        workbook.close();

        JOptionPane.showMessageDialog(this, "Xuất file thành công!");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + e.getMessage());
    }
}



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_capnhat;
    private javax.swing.JButton bt_them;
    private javax.swing.JButton bt_xoa;
    private javax.swing.JButton bt_xuat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private final javax.swing.JPanel jPanel3 = new panelvien(20, new Color(255, 255, 255));
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelquanlynhanvien;
    private javax.swing.JTable tbnhanvien;
    private javax.swing.JTextField txt_chucvu;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_tennhanvien;
    private javax.swing.JTextField txt_timkiem;
    private javax.swing.JTextField txtgioitinh;
    private javax.swing.JTextField txtma;
    // End of variables declaration//GEN-END:variables
}
