package Controller;

import javax.swing.*;
import java.awt.*;

public class RoundedTextField extends JTextField {
    private int cornerRadius = 15;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false); // Không vẽ nền mặc định
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Bóng đổ
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

        // Nền
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);

        // Vẽ chữ
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        return new Insets(8, 12, 8, 12); // Padding trong ô
    }
}
