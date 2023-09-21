package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;

    // Interface to communicate with the ClientMain
    public interface LoginListener {
        void onLogin(String username);
    }

    private LoginListener listener;

    public LoginWindow(LoginListener listener) {
        this.listener = listener;

        // Set JFrame properties
        setTitle("Login to Chat Application");
        setSize(300, 150);
        setLayout(new FlowLayout());

        // Initialize and add label for username
        JLabel usernameLabel = new JLabel("Enter Username:");
        add(usernameLabel);

        // Initialize and add username text field
        usernameField = new JTextField(20);
        add(usernameField);

        // Initialize and add login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().trim().length() > 0) {
                    listener.onLogin(usernameField.getText().trim());  // Notify ClientMain about successful login
                    dispose();  // Close the login window
                } else {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(loginButton);

        // Close operation for the JFrame
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginWindow(new LoginListener() {
                    @Override
                    public void onLogin(String username) {
                        System.out.println("Logged in as " + username);
                    }
                }).setVisible(true);
            }
        });
    }
}
