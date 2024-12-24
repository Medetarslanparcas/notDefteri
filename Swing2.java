package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Swing2 {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Oturum Açma");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JLabel titleLabel = new JLabel("Not Defteri Giriş");
        titleLabel.setBounds(150, 20, 250, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        userLabel.setBounds(100, 80, 100, 25);

        JTextField userText = new JTextField();
        userText.setBounds(220, 80, 180, 25);
        JLabel passLabel = new JLabel("Şifre:");

        passLabel.setBounds(100, 120, 100, 25);
        JPasswordField passText = new JPasswordField();
        passText.setBounds(220, 120, 180, 25);


        JButton loginButton = new JButton("Giriş Yap");
        loginButton.setBounds(200, 170, 120, 30);
        loginButton.setBackground(new Color(255, 153, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);


        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setBounds(150, 210, 200, 25);


        frame.add(titleLabel);
        frame.add(userLabel);
        frame.add(userText);
        frame.add(passLabel);
        frame.add(passText);
        frame.add(loginButton);
        frame.add(resultLabel);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passText.getPassword());


                try (Connection connection = DatabaseConnection.connect()) {
                    String query = "SELECT * FROM tbl_kullanicilar WHERE kullanici_Ad = ? AND kullanici_sifre = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        resultLabel.setText("Giriş Başarılı");
                        resultLabel.setForeground(new Color(0, 128, 0));


                        Swing1.main(null);
                        frame.dispose();
                    } else {
                        resultLabel.setText("Lütfen bilgilerinizi tekrar giriniz");
                        resultLabel.setForeground(Color.RED);
                    }
                } catch (SQLException ex) {
                    resultLabel.setText("Veritabanı hatası");
                    resultLabel.setForeground(Color.RED);
                    ex.printStackTrace();
                }
            }
        });


        frame.setVisible(true);
    }
}
