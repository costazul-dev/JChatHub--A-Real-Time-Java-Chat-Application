package Client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain implements LoginWindow.LoginListener {
    // Declare these variables at the class level
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void onLogin(String username) {
        try {
            // Initialize the socket, input, and output streams
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send the username to the server
            out.println(username);

            // Create a ChatWindow object and pass the PrintWriter and username to it
            ChatWindow chatWindow = new ChatWindow(out, username);

            // Make the chat window visible
            chatWindow.setVisible(true);

            // Start a new thread to read messages from the server
            new Thread(() -> {
                String receivedMessage;
                try {
                    while ((receivedMessage = in.readLine()) != null) {
                        processServerMessage(chatWindow, receivedMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to process messages from the server
    private void processServerMessage(ChatWindow chatWindow, String message) {
        // Check for special commands (e.g., for updating the client list)
        if (message.startsWith("::clients::")) {
            String[] clients = message.substring("::clients::".length()).split(",");
            chatWindow.updateClientsList(clients);
        } else {
            chatWindow.displayMessage(message);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Show the login window first
            new LoginWindow(new ClientMain()).setVisible(true);
        });
    }
}
