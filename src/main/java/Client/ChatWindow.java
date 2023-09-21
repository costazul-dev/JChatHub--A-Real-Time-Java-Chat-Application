package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

public class ChatWindow extends JFrame {
    // Declare UI elements
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton disconnectButton;
    private JList<String> clientsList;
    private DefaultListModel<String> clientsListModel;

    // PrintWriter to send messages to the server
    private PrintWriter out;

    // Variable to store username
    private String username;

    public ChatWindow(PrintWriter out, String username) {
        // Initialize the PrintWriter and username
        this.out = out;
        this.username = username;

        setTitle("Chat Application - Logged in as " + username);
        setSize(400, 300);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());

        textField = new JTextField();
        southPanel.add(textField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.setEnabled(out != null);  // Disable if out is null
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        disconnectButton = new JButton("Disconnect");
        disconnectButton.setEnabled(out != null);  // Disable if out is null
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("::shutdown::");
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(sendButton);
        buttonPanel.add(disconnectButton);
        southPanel.add(buttonPanel, BorderLayout.EAST);

        clientsListModel = new DefaultListModel<>();
        clientsList = new JList<>(clientsListModel);
        add(new JScrollPane(clientsList), BorderLayout.EAST);

        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void sendMessage() {
        String message = textField.getText();
        if (message != null && !message.trim().isEmpty()) {
            out.println(message);
            textField.setText("");
        }
    }

    public void displayMessage(String message) {
        textArea.append(message + "\n");  // Changed from "Other: " + message
    }

    public void updateClientsList(String[] clients) {
        clientsListModel.clear();
        for (String client : clients) {
            clientsListModel.addElement(client);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatWindow(null, "TestUser").setVisible(true);
        });
    }
}
