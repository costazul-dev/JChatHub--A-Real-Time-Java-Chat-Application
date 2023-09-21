/* package GptClient;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

public class Gpt3ApiClient {
    private final String apiKey = "YOUR_OPENAI_API_KEY_HERE";
    private final OkHttpClient httpClient = new OkHttpClient();

    public String sendMessage(String inputText) throws IOException {
        String apiEndpoint = "https://api.openai.com/v1/engines/davinci-codex/completions"; // Replace with the appropriate API endpoint
        String payload = new JSONObject()
                .put("prompt", inputText)
                .put("max_tokens", 100)
                .toString();

        RequestBody requestBody = RequestBody.create(payload, okhttp3.MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(apiEndpoint)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(requestBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseData = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseData);
            return jsonResponse.getJSONObject("choices").getJSONArray("text").getString(0);
        }
    }
}
*/
