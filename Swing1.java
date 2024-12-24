package swing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Swing1 {

    public static void main(String[] args) {

        JFrame jf = new JFrame("Not Defteri");
        jf.setSize(800, 600);
        jf.setLocationRelativeTo(null); // Pencereyi ortada aç
        jf.setLayout(new BorderLayout());

        SaveManager saveManager = new SaveManager();


        JTabbedPane tabbedPane = new JTabbedPane();


        JTextArea notIcerikArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(notIcerikArea);
        tabbedPane.add("Sayfa 1", scrollPane);

        int[] sayfaSayisi = {2};

        jf.add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton yeniSayfaButton = ButtonFactory.createStyledButton("Yeni Sayfa", e -> {
            JTextArea newTextArea = new JTextArea(10, 30);
            JScrollPane newScrollPane = new JScrollPane(newTextArea);
            tabbedPane.add("Sayfa " + sayfaSayisi[0], newScrollPane);
            sayfaSayisi[0]++;
        });
        yeniSayfaButton.setBackground(Color.GREEN);
        buttonPanel.add(yeniSayfaButton);

        JButton sayfayiKapatButton = ButtonFactory.createStyledButton("Sayfayı Kapat", e -> {
            int seciliSekme = tabbedPane.getSelectedIndex();
            if (seciliSekme >= 0) {
                tabbedPane.removeTabAt(seciliSekme);
                yenidenNumaralandir(tabbedPane, sayfaSayisi);
            }
        });
        sayfayiKapatButton.setBackground(Color.GREEN);
        buttonPanel.add(sayfayiKapatButton);

        JButton sifreGuncelleButton = ButtonFactory.createStyledButton("Şifre Güncelle", e -> {
            Swing4.main(null);
        });
        sifreGuncelleButton.setBackground(Color.ORANGE);
        buttonPanel.add(sifreGuncelleButton);

        JButton notGuncelleButton = ButtonFactory.createStyledButton("Not Güncelle", e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                JScrollPane currentScrollPane = (JScrollPane) tabbedPane.getComponentAt(selectedIndex);
                JTextArea currentTextArea = (JTextArea) currentScrollPane.getViewport().getView();
                String noticerik = currentTextArea.getText();

                if (noticerik.isEmpty()) {
                    JOptionPane.showMessageDialog(jf, "Not boş olamaz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "UPDATE tbl_notlar SET tbl_noticerik = ? WHERE tbl_notlarID=?";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, noticerik);
                        stmt.setInt(2, selectedIndex + 1);
                        int rowsUpdated = stmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(jf, "Not başarıyla güncellendi!", "Güncelle", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(jf, "Not güncellenemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(jf, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(jf, "Güncellenecek bir sekme yok.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            }
        });
        notGuncelleButton.setBackground(Color.YELLOW);
        buttonPanel.add(notGuncelleButton);
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = ButtonFactory.createStyledButton("Kaydet", e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex >= 0) {
                JScrollPane currentScrollPane = (JScrollPane) tabbedPane.getComponentAt(selectedIndex);
                JTextArea currentTextArea = (JTextArea) currentScrollPane.getViewport().getView();
                String noticerik = currentTextArea.getText();

                if (noticerik.isEmpty()) {
                    JOptionPane.showMessageDialog(jf, "Not boş olamaz.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try (Connection conn = DatabaseConnection.connect()) {

                    String sql = "INSERT INTO tbl_notlar (tbl_noticerik) VALUES (?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, noticerik);
                        int rowsInserted = stmt.executeUpdate();

                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(jf, "Not başarıyla kaydedildi!", "Kaydet", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(jf, "Not kaydedilemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(jf, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveButton.setBackground(Color.GREEN);
        topRightPanel.add(saveButton);
        jf.add(topRightPanel, BorderLayout.NORTH);
        jf.add(buttonPanel, BorderLayout.SOUTH);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private static void yenidenNumaralandir(JTabbedPane tabbedPane, int[] sayfaSayisi) {
        sayfaSayisi[0] = 1;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setTitleAt(i, "Sayfa " + sayfaSayisi[0]);
            sayfaSayisi[0]++;
        }
    }
}
