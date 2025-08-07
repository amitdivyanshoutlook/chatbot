package com.perplexity.perplexity.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TranslateService {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String translate(String text, String sourceLang, String targetLang) throws IOException {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String url = "https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=" + sourceLang + "|" + targetLang;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JsonNode rootNode = mapper.readTree(responseBody);
            return rootNode.path("responseData").path("translatedText").asText();
        }
    }
}
