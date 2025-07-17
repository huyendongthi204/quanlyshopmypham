package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */

    
import java.awt.*;
import javax.swing.*;

public class panelvien extends JPanel {
    private Color backgroundColor;
    private int cornerRadius = 15;

    public panelvien(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    public panelvien(int radius, Color bgColor) {
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Bóng đổ nhẹ
        graphics.setColor(new Color(0, 0, 0, 30)); 
        graphics.fillRoundRect(5, 5, width - 10, height - 10, arcs.width, arcs.height);

        // Màu nền panel
        graphics.setColor(backgroundColor != null ? backgroundColor : getBackground());
        graphics.fillRoundRect(0, 0, width - 10, height - 10, arcs.width, arcs.height);
    }


    
}
