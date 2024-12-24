package swing;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ComponentFactory {

    public static JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    public static JScrollPane createTab(String tabName, JTabbedPane tabbedPane, int[] sayfaSayisi) {
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        tabbedPane.addTab(tabName + " " + sayfaSayisi[0], scrollPane);
        sayfaSayisi[0]++;
        return scrollPane;
    }
}
