package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Set<PrintWriter> clientWriters;
    private Set<String> clientUsernames;

    public ClientHandler(Socket socket, PrintWriter out, Set<PrintWriter> clientWriters, Set<String> clientUsernames) {
        this.clientSocket = socket;
        this.out = out;
        this.clientWriters = clientWriters;
        this.clientUsernames = clientUsernames;
    }

    private void broadcastClientsList() {
        String clientsList = "::clients::" + String.join(",", clientUsernames);
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(clientsList);
            }
        }
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String receivedMessage;

            // Read username and add to list
            String username = in.readLine();
            synchronized (clientUsernames) {
                clientUsernames.add(username);
            }
            broadcastClientsList();

            // Log the new connection
            System.out.println("New connection from client: " + username);

            while ((receivedMessage = in.readLine()) != null) {
                // Log the received message
                System.out.println(username + " sent: " + receivedMessage);

                if ("::shutdown::".equals(receivedMessage)) {
                    System.out.println(username + " sent a shutdown signal. Closing client connection.");
                    synchronized (clientUsernames) {
                        clientUsernames.remove(username);
                    }
                    broadcastClientsList();
                    out.println("You have disconnected.");
                    break;
                }

                synchronized (clientWriters) {
                    for (PrintWriter writer : clientWriters) {
                        writer.println(username + ": " + receivedMessage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Client error: " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
