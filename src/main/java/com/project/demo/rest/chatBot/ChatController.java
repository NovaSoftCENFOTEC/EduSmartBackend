package com.project.demo.rest.chatBot;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String LM_STUDIO_API_URL = "http://localhost:1234/v1/chat/completions";

    @PostMapping
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Message cannot be empty"));
        }

        try {
            Map<String, Object> lmStudioRequest = new HashMap<>();
            lmStudioRequest.put("model", "local-model");
            List<Map<String, String>> messages = new ArrayList<>();

            messages.add(Map.of("role", "system", "content",
                    "Eres un chatbot educativo especializado en historia. " +
                            "Tu objetivo es explicar eventos, personajes y conceptos históricos de manera sencilla y fácil de entender para estudiantes de todas las edades. " +
                            "Siempre responde únicamente sobre temas de historia. " +
                            "Si te preguntan algo que no sea de historia, responde amablemente que tu especialidad es la historia y que solo puedes hablar de eso."
            ));

            messages.add(Map.of("role", "user", "content", userMessage));
            lmStudioRequest.put("messages", messages);
            double TEMPERATURE = 0.7;
            lmStudioRequest.put("temperature", TEMPERATURE);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(lmStudioRequest, headers);

            ResponseEntity<Map> lmStudioResponse = restTemplate.postForEntity(
                    LM_STUDIO_API_URL,
                    entity,
                    Map.class
            );

            if (lmStudioResponse.getStatusCode().is2xxSuccessful() && lmStudioResponse.getBody() != null) {
                Map<String, Object> responseBody = lmStudioResponse.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, String> message = (Map<String, String>) ((Map<String, Object>) choices.get(0)).get("message");
                    String botResponse = message.get("content");
                    return ResponseEntity.ok(Map.of("response", botResponse));
                }
            }
            return ResponseEntity.status(500).body(Map.of("error", "Could not get a valid response from LM Studio."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error communicating with LM Studio: " + e.getMessage()));
        }
    }
}
