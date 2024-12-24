package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonFactory {


    public static JButton createStyledButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                g2.setColor(new Color(30, 144, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);


                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            public void setContentAreaFilled(boolean b) {
            }
        };

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        button.setOpaque(false);
        button.addActionListener(actionListener);

        return button;
    }
}
