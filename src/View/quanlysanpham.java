/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Controller.NhanVienDAO;
import Controller.RoundedTextField;
import Controller.SanPhamDao;
import Controller.panelvien;
import Model.NhanVien;
import Model.SanPham;
import Model.connect;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author PC
 */
public class quanlysanpham extends javax.swing.JPanel {

    /**
     * Creates new form quanlysanpham
     */
    public quanlysanpham() throws SQLException {
        initComponents();
         tbsanpham.getTableHeader().setBackground(new java.awt.Color(255, 228, 214)); // 🍑 hồng cam nhạt
        tbsanpham.getTableHeader().setForeground(new java.awt.Color(0, 0, 0));       // 🖤 chữ đen

        tbsanpham.getTableHeader().setFont(new java.awt.Font("Segoe UI", Font.BOLD, 15));
            setPlaceholder(txtma, "Mã San Pham");
            setPlaceholder(txt_tensanpham, "Tên Sản Phẩm");
            setPlaceholder(txtdongia, "Đơn Giá");
            setPlaceholder(txt_soluongton, "Số Lượng Tồn");
            setPlaceholder(txt_donvitinh, "Đơn Vị Tính");
            //setPlaceholder(lb_anh, "Ảnh sản phẩm");
            setPlaceholder(txt_timkiem, "Nhập từ khóa tìm kiếm (tên, mã...)");
            loadTableSanPham();
            canGiuaTable();
     tbsanpham.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = tbsanpham.getSelectedRow();
        if (selectedRow >= 0) {
            hienThongTinLenTextField(selectedRow);
        }
    }
});
     txt_timkiem.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) {
        locTheoTuKhoa();
    }

    public void removeUpdate(DocumentEvent e) {
        locTheoTuKhoa();
    }

    public void changedUpdate(DocumentEvent e) {
        locTheoTuKhoa();
    }
});
     lb_anh.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh sản phẩm");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg", "gif"));

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Copy ảnh vào thư mục dự án (ví dụ: src/Image/anhsanpham/)
            String folderPath = "src/Image/anhsanpham/";
            File targetFile = new File(folderPath + selectedFile.getName());

            try {
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi sao chép ảnh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Hiển thị ảnh trong JLabel
            ImageIcon icon = new ImageIcon(targetFile.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(lb_anh.getWidth(), lb_anh.getHeight(), Image.SCALE_SMOOTH);
            lb_anh.setIcon(new ImageIcon(img));

            // Gán tên file ảnh vào textfield
            lb_anh.setText(selectedFile.getName());
        }
    }
});
    
    
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
     public void loadTableSanPham() throws SQLException {
    // Khởi tạo DAO
    Connection conn = connect.getConnecttion();
    SanPhamDao dao = new SanPhamDao();

    // Lấy dữ liệu
    List<SanPham> list = dao.getAllSanPham();

    // Đổ dữ liệu lên JTable
    DefaultTableModel model = (DefaultTableModel) tbsanpham.getModel(); // tên JTable bạn đặt
    model.setRowCount(0); // Xóa dữ liệu cũ

    for (SanPham sp : list) {
        model.addRow(new Object[]{
            sp.getMaSP(),
            sp.getTenSP(),
            sp.getDonGia(),
            sp.getSoLuongTon(),
            sp.getDonViTinh(),
            sp.getHinhAnh() // nếu là đường dẫn
        });
    }
}
 public void canGiuaTable() {
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.CENTER);
    tbsanpham.setShowGrid(true); // Hiển thị lưới
    tbsanpham.setGridColor(Color.GRAY); // Màu viền

    renderer.setForeground(new Color(0, 0, 0)); // 🎨 Màu chữ hồng đậm

for (int i = 0; i < tbsanpham.getColumnCount(); i++) {
    tbsanpham.getColumnModel().getColumn(i).setCellRenderer(renderer);
}

}
  private void hienThongTinLenTextField(int row) {
    txtma.setText(tbsanpham.getValueAt(row, 0).toString());
    txt_tensanpham.setText(tbsanpham.getValueAt(row, 1).toString());
    txtdongia.setText(tbsanpham.getValueAt(row, 2).toString());
    txt_soluongton.setText(tbsanpham.getValueAt(row, 3).toString());
    txt_donvitinh.setText(tbsanpham.getValueAt(row, 4).toString());
    
    // Lấy tên ảnh từ cột 5
    Object val5 = tbsanpham.getValueAt(row, 5);
    String tenFileAnh = (val5 != null) ? val5.toString() : "";

    // Tạo đường dẫn ảnh
    String duongDanAnh = "src/Image/anhsanpham/" + tenFileAnh;

    // Kiểm tra và gán ảnh vào JLabel
    File fileAnh = new File(duongDanAnh);
    if (fileAnh.exists() && !tenFileAnh.isEmpty()) {
        ImageIcon icon = new ImageIcon(duongDanAnh);
        Image img = icon.getImage().getScaledInstance(lb_anh.getWidth(), lb_anh.getHeight(), Image.SCALE_SMOOTH);
        lb_anh.setIcon(new ImageIcon(img));
    } else {
        lb_anh.setIcon(null);
        System.out.println("Không tìm thấy ảnh: " + duongDanAnh);
    }
}

 private void clearForm() {
    txtma.setText("Mã Sản Phẩm");
    txtma.setForeground(Color.GRAY);

    txt_tensanpham.setText("Tên Sản Phẩm");
   txt_tensanpham.setForeground(Color.GRAY);

    txtdongia.setText("Đơn Giá");
    txtdongia.setForeground(Color.GRAY);

    txt_soluongton.setText("Số Lượng Tồn");
    txt_soluongton.setForeground(Color.GRAY);

   txt_donvitinh.setText("Đơn Vị Tính");
   txt_donvitinh.setForeground(Color.GRAY);

    lb_anh.setText("Ảnh sản phẩm");
    lb_anh.setForeground(Color.GRAY);
}
 private void xoaSanPham() throws SQLException {
    String maSP = txtma.getText().trim();

    if (maSP.isEmpty() || maSP.equals("Mã SP")) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
        "Bạn có chắc muốn xóa sản phẩm có mã: " + maSP + "?",
        "Xác nhận xóa",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        SanPhamDao dao = new SanPhamDao();
        if (dao.deleteSanPham(maSP)) {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
            loadTableSanPham(); // load lại bảng sau khi xóa
            clearForm();        // xóa thông tin trong form nếu cần
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại.");
        }
    }
}

 public void themSanPhamTuPopup(JDialog dialog,
                               JTextField tfMaSP,
                               JTextField tfTenSP,
                               JTextField tfDonGia,
                               JTextField tfSoLuongTon,
                               JTextField tfDonViTinh,
                               JTextField tfHinhAnh) {

    String maSP = tfMaSP.getText().trim();
    String tenSP = tfTenSP.getText().trim();
    String donGiaStr = tfDonGia.getText().trim();
    String soLuongStr = tfSoLuongTon.getText().trim();
    String donViTinh = tfDonViTinh.getText().trim();
    String hinhAnh = tfHinhAnh.getText().trim();

    // Kiểm tra rỗng
    if (maSP.isEmpty() || tenSP.isEmpty() || donGiaStr.isEmpty()
            || soLuongStr.isEmpty() || donViTinh.isEmpty() || hinhAnh.isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin sản phẩm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Kiểm tra số
    double donGia;
    int soLuong;
    try {
        donGia = Double.parseDouble(donGiaStr);
        soLuong = Integer.parseInt(soLuongStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(dialog, "Đơn giá hoặc số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    SanPhamDao dao = new SanPhamDao();

    try {
        if (dao.isMaSPTonTai(maSP)) {
            JOptionPane.showMessageDialog(dialog, "Mã sản phẩm đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SanPham sp = new SanPham();
        sp.setMaSP(maSP);
        sp.setTenSP(tenSP);
        sp.setDonGia(donGia);
        sp.setSoLuongTon(soLuong);
        sp.setDonViTinh(donViTinh);
        sp.setHinhAnh(hinhAnh);

        if (dao.insertSanPham(sp)) {
            JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadTableSanPham(); // Load lại bảng
        } else {
            JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(dialog, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
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

        panelquanlynhanvien = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtma = new RoundedTextField(20)
        ;
        txt_soluongton = new RoundedTextField(20)
        ;
        jLabel4 = new javax.swing.JLabel();
        txtdongia = new RoundedTextField(20)
        ;
        jLabel5 = new javax.swing.JLabel();
        txt_tensanpham = new RoundedTextField(20)
        ;
        jLabel6 = new javax.swing.JLabel();
        txt_donvitinh = new RoundedTextField(20)
        ;
        jLabel7 = new javax.swing.JLabel();
        bt_them = new javax.swing.JButton();
        bt_xoa = new javax.swing.JButton();
        bt_capnhat = new javax.swing.JButton();
        bt_xuat = new javax.swing.JButton();
        lb_anh = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbsanpham = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_timkiem = new javax.swing.JTextField();

        panelquanlynhanvien.setBackground(new java.awt.Color(255, 230, 242));
        panelquanlynhanvien.setRequestFocusEnabled(false);
        panelquanlynhanvien.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Mã Sản Phẩm  ");

        jLabel3.setText("Tên Sản Phẩm ");

        txtma.setForeground(new java.awt.Color(204, 204, 204));
        txtma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmaActionPerformed(evt);
            }
        });

        txt_soluongton.setForeground(new java.awt.Color(204, 204, 204));

        jLabel4.setText("Đơn Giá");

        txtdongia.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        txtdongia.setForeground(new java.awt.Color(204, 204, 204));

        jLabel5.setText("Số Lượng Tồn");

        txt_tensanpham.setForeground(new java.awt.Color(204, 204, 204));

        jLabel6.setText("Đơn Vị Tính");

        txt_donvitinh.setForeground(new java.awt.Color(153, 153, 153));

        jLabel7.setText("Hình Ảnh");

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

        lb_anh.setText("Ảnh sản phẩm");
        lb_anh.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 102, 0)));

        tbsanpham.setForeground(new java.awt.Color(255, 153, 204));
        tbsanpham.setModel(new javax.swing.table.DefaultTableModel(
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
                "Mã Sản Phẩm", "Tên Sản Phẩm", "Đơn Giá ", "Số Lượng Tồn ", "Đơn Vị Tính", "Hình Ảnh"
            }
        ));
        jScrollPane2.setViewportView(tbsanpham);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtma, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                    .addComponent(txt_tensanpham)
                                    .addComponent(txt_soluongton)
                                    .addComponent(txtdongia))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(47, 47, 47)
                                        .addComponent(jLabel7)
                                        .addGap(64, 64, 64)
                                        .addComponent(lb_anh, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addGap(37, 37, 37)
                                        .addComponent(txt_donvitinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(52, 52, 52))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(bt_them, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(bt_xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(bt_capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(bt_xuat)))
                        .addGap(0, 79, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_tensanpham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtdongia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel7)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txt_soluongton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_donvitinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_anh, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_them, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_xoa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_capnhat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_xuat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(212, 45, 126));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/icon/icons8-online-shopping-100.png"))); // NOI18N
        jLabel1.setText("QUẢN LÝ SẢN PHẨM");

        jLabel8.setText("Tìm Kiếm Sản Phẩm");

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
                        .addGap(26, 26, 26)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(79, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelquanlynhanvien.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 20, 910, 510));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 992, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelquanlynhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 569, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelquanlynhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bt_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_themActionPerformed
        // TODO add your handling code here:
      JDialog dialog = new JDialog((JFrame) null, "Thêm Sản Phẩm", true);
    dialog.setSize(550, 380);
    dialog.setLocationRelativeTo(null);
    dialog.setLayout(new BorderLayout());

    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 5, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel lblTitle = new JLabel("➕ Thêm Sản Phẩm");
    lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitle.setForeground(new Color(0, 153, 0)); // màu xanh lá đậm

    dialog.add(lblTitle, BorderLayout.NORTH);

    // Các trường nhập liệu
    JTextField tfMaSP = new JTextField();
    JTextField tfTenSP = new JTextField();
    JTextField tfDonGia = new JTextField();
    JTextField tfSoLuongTon = new JTextField();
    JTextField tfDonViTinh = new JTextField();
    JTextField tfHinhAnh = new JTextField();

    // Hàng 1
    gbc.gridx = 0; gbc.gridy = 0;
    contentPanel.add(new JLabel("Mã SP:"), gbc);
    gbc.gridx = 1;
    contentPanel.add(tfMaSP, gbc);

    gbc.gridx = 2;
    contentPanel.add(new JLabel("Tên SP:"), gbc);
    gbc.gridx = 3;
    contentPanel.add(tfTenSP, gbc);

    // Hàng 2
    gbc.gridx = 0; gbc.gridy = 1;
    contentPanel.add(new JLabel("Đơn Giá:"), gbc);
    gbc.gridx = 1;
    contentPanel.add(tfDonGia, gbc);

    gbc.gridx = 2;
    contentPanel.add(new JLabel("Số Lượng Tồn:"), gbc);
    gbc.gridx = 3;
    contentPanel.add(tfSoLuongTon, gbc);

    // Hàng 3
    gbc.gridx = 0; gbc.gridy = 2;
    contentPanel.add(new JLabel("Đơn Vị Tính:"), gbc);
    gbc.gridx = 1;
    contentPanel.add(tfDonViTinh, gbc);

    gbc.gridx = 2;
    contentPanel.add(new JLabel("Tên Ảnh:"), gbc);
    gbc.gridx = 3;
    contentPanel.add(tfHinhAnh, gbc);

    // Nút Lưu
    JButton btnLuu = new JButton("Lưu");
    btnLuu.setBackground(new Color(0, 153, 0));
    btnLuu.setForeground(Color.WHITE);
    btnLuu.setFocusPainted(false);
    btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));

    gbc.gridx = 0; gbc.gridy = 3;
    gbc.gridwidth = 4;
    gbc.anchor = GridBagConstraints.CENTER;
    contentPanel.add(btnLuu, gbc);

    // Sự kiện nút lưu
    btnLuu.addActionListener(ev -> {
        themSanPhamTuPopup(dialog, tfMaSP, tfTenSP, tfDonGia, tfSoLuongTon, tfDonViTinh, tfHinhAnh);
        // Không cần dialog.dispose() ở đây vì đã có trong hàm xử lý nếu thành công
    });

    dialog.add(contentPanel, BorderLayout.CENTER);
    dialog.setVisible(true);
    }//GEN-LAST:event_bt_themActionPerformed

    private void bt_xoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_xoaActionPerformed
        // TODO add your handling code here:
        bt_xoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    xoaSanPham();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(quanlysanpham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
               
            }
        });
    }//GEN-LAST:event_bt_xoaActionPerformed
private void capNhatSanPham() throws SQLException {
    String maSP = txtma.getText().trim();

    // Kiểm tra nhập liệu
    if (maSP.isEmpty() || maSP.equals("Mã SP") ||
        txt_tensanpham.getText().trim().isEmpty() ||
        txtdongia.getText().trim().isEmpty() ||
        txt_soluongton.getText().trim().isEmpty() ||
        txt_donvitinh.getText().trim().isEmpty() ||
        lb_anh.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin để cập nhật sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Kiểm tra dữ liệu số
    double donGia;
    int soLuong;
    try {
        donGia = Double.parseDouble(txtdongia.getText().trim());
        soLuong = Integer.parseInt(txt_soluongton.getText().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Đơn giá hoặc số lượng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Xác nhận người dùng
    int choice = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn cập nhật sản phẩm này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (choice != JOptionPane.YES_OPTION) return;

    // Tạo đối tượng sản phẩm
    SanPham sp = new SanPham();
    sp.setMaSP(maSP);
    sp.setTenSP(txt_tensanpham.getText().trim());
    sp.setDonGia(donGia);
    sp.setSoLuongTon(soLuong);
    sp.setDonViTinh(txt_donvitinh.getText().trim());
    sp.setHinhAnh(lb_anh.getText().trim());

    // Gọi DAO để cập nhật
    SanPhamDao dao = new SanPhamDao();
    if (dao.updateSanPham(sp)) {
        JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
        loadTableSanPham(); // refresh bảng
        clearForm();        // xóa form
    } else {
        JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thất bại.");
    }
}

    private void bt_capnhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_capnhatActionPerformed
        try {
            capNhatSanPham();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(quanlysanpham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bt_capnhatActionPerformed

    private void bt_xuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_xuatActionPerformed
         //TODO add your handling code here:
        bt_xuat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
                fileChooser.setSelectedFile(new File("sanpham.xlsx"));

                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    exportToExcel(tbsanpham,fileToSave);
                }
            }

        });
    }//GEN-LAST:event_bt_xuatActionPerformed

    private void txt_timkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_timkiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_timkiemActionPerformed

    private void txtmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmaActionPerformed
private void locTheoTuKhoa() {
    String keyword = txt_timkiem.getText().trim().toLowerCase();

    DefaultTableModel model = (DefaultTableModel) tbsanpham.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    tbsanpham.setRowSorter(sorter);

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
    private final javax.swing.JPanel jPanel3 = new panelvien(20, new Color(255, 255, 255));
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lb_anh;
    private javax.swing.JPanel panelquanlynhanvien;
    private javax.swing.JTable tbsanpham;
    private javax.swing.JTextField txt_donvitinh;
    private javax.swing.JTextField txt_soluongton;
    private javax.swing.JTextField txt_tensanpham;
    private javax.swing.JTextField txt_timkiem;
    private javax.swing.JTextField txtdongia;
    private javax.swing.JTextField txtma;
    // End of variables declaration//GEN-END:variables
}
