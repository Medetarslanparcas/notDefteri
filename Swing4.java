package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Swing4 {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Şifre Güncelle");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(240, 240, 240));


        JLabel titleLabel = new JLabel("Şifre Güncelle");
        titleLabel.setBounds(150, 20, 200, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));


        JLabel nameLabel = new JLabel("Ad:");
        nameLabel.setBounds(100, 70, 100, 25);
        JTextField nameText = new JTextField();
        nameText.setBounds(220, 70, 180, 25);


        JLabel surnameLabel = new JLabel("Soyad:");
        surnameLabel.setBounds(100, 110, 100, 25);
        JTextField surnameText = new JTextField();
        surnameText.setBounds(220, 110, 180, 25);


        JLabel passLabel = new JLabel("Yeni Şifre:");
        passLabel.setBounds(100, 150, 100, 25);
        JPasswordField passText = new JPasswordField();
        passText.setBounds(220, 150, 180, 25);

        JLabel passConfirmLabel = new JLabel("Şifre Tekrar:");
        passConfirmLabel.setBounds(100, 190, 100, 25);
        JPasswordField passConfirmText = new JPasswordField();
        passConfirmText.setBounds(220, 190, 180, 25);


        JButton updateButton = new JButton("Güncelle");
        updateButton.setBounds(200, 240, 120, 30);
        updateButton.setBackground(Color.GREEN);  // Buton rengi yeşil yapıldı
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);


        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setBounds(150, 280, 200, 25);


        frame.add(titleLabel);
        frame.add(nameLabel);
        frame.add(nameText);
        frame.add(surnameLabel);
        frame.add(surnameText);
        frame.add(passLabel);
        frame.add(passText);
        frame.add(passConfirmLabel);
        frame.add(passConfirmText);
        frame.add(updateButton);
        frame.add(resultLabel);


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText();
                String surname = surnameText.getText();
                String password = new String(passText.getPassword());
                String confirmPassword = new String(passConfirmText.getPassword());

                if (name.isEmpty() || surname.isEmpty() || password.isEmpty()) {
                    resultLabel.setText("Lütfen tüm alanları doldurun!");
                    resultLabel.setForeground(Color.RED);
                } else if (!password.equals(confirmPassword)) {
                    resultLabel.setText("Şifreler eşleşmiyor!");
                    resultLabel.setForeground(Color.RED);
                } else {

                    try (Connection conn = DatabaseConnection.connect()) {

                        String checkUserSql = "SELECT * FROM tbl_kullanicilar WHERE kullanici_Ad = ? AND kullanici_Soyad = ?";
                        try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                            checkStmt.setString(1, name);
                            checkStmt.setString(2, surname);
                            ResultSet rs = checkStmt.executeQuery();

                            if (rs.next()) {

                                String updateSql = "UPDATE tbl_kullanicilar SET kullanici_sifre = ? WHERE kullanici_Ad = ? AND kullanici_Soyad = ?";
                                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                                    updateStmt.setString(1, password);
                                    updateStmt.setString(2, name);
                                    updateStmt.setString(3, surname);

                                    int rowsUpdated = updateStmt.executeUpdate();
                                    if (rowsUpdated > 0) {
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                resultLabel.setText("Şifre başarıyla güncellendi.");
                                                resultLabel.setForeground(new Color(0, 128, 0));
                                            }
                                        });
                                    } else {
                                        SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                resultLabel.setText("Şifre güncellenemedi.");
                                                resultLabel.setForeground(Color.RED);
                                            }
                                        });
                                    }
                                }
                            } else {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        resultLabel.setText("Kullanıcı bulunamadı.");
                                        resultLabel.setForeground(Color.RED);
                                    }
                                });
                            }
                        }
                    } catch (SQLException ex) {

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                resultLabel.setText("Veritabanı hatası: " + ex.getMessage());
                                resultLabel.setForeground(Color.RED);
                            }
                        });
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}
