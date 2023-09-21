package Server;

// Import necessary packages

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    // Initialize GPT-3 client
    // Gpt3ApiClient gpt3Client = new Gpt3ApiClient();

    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Set<String> clientUsernames = new HashSet<>();  // Added this line to store usernames

    // Method to broadcast updated list of usernames to all connected clients
    private static void broadcastUsernames() {
        String clientsList = String.join(",", clientUsernames);
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println("::clients::" + clientsList);
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook running...");
            executorService.shutdown();
        }));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getRemoteSocketAddress());

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                // Modified this line to pass clientUsernames to the ClientHandler constructor
                ClientHandler clientHandler = new ClientHandler(clientSocket, out, clientWriters, clientUsernames);
                executorService.submit(clientHandler);

                // Broadcast updated usernames list
                broadcastUsernames();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing server: " + e.getMessage());
        }
    }
}
