package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Swing3 {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Kullanıcı Kaydı");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(240, 240, 240));


        JLabel titleLabel = new JLabel("KULLANICI KAYDI");
        titleLabel.setBounds(130, 20, 300, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel nameLabel = new JLabel("Ad:");
        nameLabel.setBounds(100, 70, 100, 25);
        JTextField nameText = new JTextField();
        nameText.setBounds(220, 70, 180, 25);

        JLabel surnameLabel = new JLabel("Soyad:");
        surnameLabel.setBounds(100, 110, 100, 25);
        JTextField surnameText = new JTextField();
        surnameText.setBounds(220, 110, 180, 25);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(100, 150, 100, 25);
        JTextField emailText = new JTextField();
        emailText.setBounds(220, 150, 180, 25);

        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setBounds(100, 190, 100, 25);
        JPasswordField passText = new JPasswordField();
        passText.setBounds(220, 190, 180, 25);

        JLabel passConfirmLabel = new JLabel("Şifre Tekrar:");
        passConfirmLabel.setBounds(100, 230, 100, 25);
        JPasswordField passConfirmText = new JPasswordField();
        passConfirmText.setBounds(220, 230, 180, 25);

        JButton registerButton = new JButton("Kayıt Ol");
        registerButton.setBounds(200, 280, 120, 30);
        registerButton.setBackground(new Color(255, 153, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setBounds(150, 330, 200, 25);

        frame.add(titleLabel);
        frame.add(nameLabel);
        frame.add(nameText);
        frame.add(surnameLabel);
        frame.add(surnameText);
        frame.add(emailLabel);
        frame.add(emailText);
        frame.add(passLabel);
        frame.add(passText);
        frame.add(passConfirmLabel);
        frame.add(passConfirmText);
        frame.add(registerButton);
        frame.add(resultLabel);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameText.getText();
                String surname = surnameText.getText();
                String email = emailText.getText();
                String password = new String(passText.getPassword());
                String confirmPassword = new String(passConfirmText.getPassword());


                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    resultLabel.setText("Lütfen tüm alanları doldurun!");
                    resultLabel.setForeground(Color.RED);
                } else if (!password.equals(confirmPassword)) {
                    resultLabel.setText("Şifreler eşleşmiyor!");
                    resultLabel.setForeground(Color.RED);
                } else {

                    try (Connection conn = DatabaseConnection.connect()) {
                        String sql = "INSERT INTO tbl_kullanicilar (kullanici_Ad, kullanici_Soyad, kullanici_email, kullanici_sifre) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setString(1, name);
                            stmt.setString(2, surname);
                            stmt.setString(3, email);
                            stmt.setString(4, password);

                            int rowsInserted = stmt.executeUpdate();
                            if (rowsInserted > 0) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        resultLabel.setText("Kayıt Başarılı!");
                                        resultLabel.setForeground(new Color(0, 128, 0));
                                    }
                                });


                                frame.dispose();
                                Swing1.main(new String[]{});
                            } else {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        resultLabel.setText("Kayıt başarısız.");
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
