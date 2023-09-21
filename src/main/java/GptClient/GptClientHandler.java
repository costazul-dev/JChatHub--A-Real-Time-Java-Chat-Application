/* import GptClient.Gpt3ApiClient;
import Server.ClientHandler;

import java.io.PrintWriter;
import java.util.Set;

public class GptClientHandler extends ClientHandler {

    private final Gpt3ApiClient gpt3ApiClient;

    public GptClientHandler(PrintWriter out, Set<PrintWriter> clientWriters) {
        super(null, out, clientWriters);  // We don't need a Socket for GPT-3
        this.gpt3ApiClient = new Gpt3ApiClient();
    }

    public void messageReceived(String message) {
        try {
            String gpt3Response = gpt3ApiClient.sendMessage(message);
            sendMessageToClient(gpt3Response);
        } catch (Exception e) {
            e.printStackTrace();
            sendMessageToClient("An error occurred while communicating with GPT-3.");
        }
    }

    private void sendMessageToClient(String message) {
        // Implementation here to send the message to the chat client
    }
}
*/
