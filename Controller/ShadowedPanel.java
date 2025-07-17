/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author PC
 */
public class ShadowedPanel extends JPanel{
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        // Màu bóng tím nhạt, độ mờ
        GradientPaint shadow = new GradientPaint(
            0, 0, new Color(124, 58, 237, 120),  // tím mờ, alpha = 120
            80, 0, new Color(124, 58, 237, 0)     // trong suốt dần
        );

        g2d.setPaint(shadow);
        g2d.fillRect(0, 0, 80, getHeight());

        g2d.dispose();
    }
    
}
