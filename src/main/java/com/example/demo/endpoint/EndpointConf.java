package com.example.demo.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class HazavaoController {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String CHATGPT_URL = "https://api.openai.com/v1/chat/completions";

    @GetMapping("/hazavao")
    public String getDefinition(@RequestParam String teny) {
        if (teny == null || teny.isEmpty()) {
            return "The word cannot be empty.";
        }

        RestTemplate restTemplate = new RestTemplate();
        String prompt = "Word\"" + teny + "\" in Malagasy.";

        String requestBody = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{ \"role\": \"user\", \"content\": \"" + prompt + "\" }] }";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(CHATGPT_URL, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error retrieving the definition: " + e.getMessage();
        }
    }
}
