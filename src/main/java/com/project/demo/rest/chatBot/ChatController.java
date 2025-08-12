package com.project.demo.rest.chatBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * Controlador REST para el chatbot especializado en historia de Costa Rica.
 * Envía mensajes al modelo LM Studio y retorna la respuesta.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${lmstudio.api.url}")
    private String lmStudioApiUrl;

    private static final String MODEL_NAME = "local-model";
    private static final double TEMPERATURE = 0.7;

    private static final String SYSTEM_PROMPT =
            "Eres un asistente experto y especializado en la **historia de Costa Rica**. Tu propósito es responder preguntas de forma precisa, didáctica y clara, utilizando un lenguaje sencillo. " +
                    "Debes enfocarte exclusivamente en eventos, personajes, periodos y aspectos relevantes de la historia costarricense. " +
                    "**NO** debes responder sobre política actual, religión, entretenimiento, deportes, chismes, teorías de conspiración, temas personales o cualquier asunto que no esté directamente relacionado con la historia de Costa Rica. " +
                    "Si una pregunta no se relaciona con la historia de Costa Rica, responde amablemente que solo puedes proporcionar información sobre ese tema y sugiere que te pregunten algo al respecto.";

    private static final Set<String> GREETINGS = Set.of("hola", "saludos", "buenas");
    private static final String GREETING_RESPONSE = "¡Hola! ¿En qué puedo ayudarte hoy sobre la historia de Costa Rica?";
    private static final String ERROR_EMPTY_MESSAGE = "Message cannot be empty";
    private static final String ERROR_MODEL_RESPONSE = "No se pudo obtener una respuesta válida del modelo.";
    private static final String ERROR_LMSTUDIO_COMM = "Error comunicándose con LM Studio: ";

    /**
     * Envía un mensaje al chatbot y retorna la respuesta.
     * @param request mapa con el mensaje del usuario
     * @return respuesta del chatbot
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", ERROR_EMPTY_MESSAGE));
        }

        String lowerCaseMessage = userMessage.toLowerCase();
        if (GREETINGS.contains(lowerCaseMessage)) {
            return ResponseEntity.ok(Map.of("response", GREETING_RESPONSE));
        }

        try {
            Map<String, Object> lmStudioRequest = new HashMap<>();
            lmStudioRequest.put("model", MODEL_NAME);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
            messages.add(Map.of("role", "user", "content", userMessage));
            lmStudioRequest.put("messages", messages);
            lmStudioRequest.put("temperature", TEMPERATURE);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(lmStudioRequest, headers);

            ResponseEntity<Map> lmStudioResponse = restTemplate.postForEntity(
                    lmStudioApiUrl,
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

            return ResponseEntity.status(500).body(Map.of("error", ERROR_MODEL_RESPONSE));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", ERROR_LMSTUDIO_COMM + e.getMessage()));
        }
    }
}