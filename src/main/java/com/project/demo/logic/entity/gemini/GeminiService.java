package com.project.demo.logic.entity.gemini;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateQuizQuestions(String storyContent, int numberOfQuestions) {
        try {
            System.out.println("Iniciando generación de preguntas con Gemini...");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", new Object[]{
                    Map.of(
                            "parts", new Object[]{
                                    Map.of("text", String.format(
                                            "Genera %d preguntas de opción múltiple basadas en el siguiente contenido educativo. " +
                                                    "Cada pregunta debe tener 4 opciones, con solo una correcta. " +
                                                    "Responde ÚNICAMENTE en formato JSON con esta estructura exacta:\n" +
                                                    "{\n" +
                                                    "  \"questions\": [\n" +
                                                    "    {\n" +
                                                    "      \"question\": \"Texto de la pregunta\",\n" +
                                                    "      \"options\": [\n" +
                                                    "        {\"text\": \"Opción A\", \"correct\": false},\n" +
                                                    "        {\"text\": \"Opción B\", \"correct\": true},\n" +
                                                    "        {\"text\": \"Opción C\", \"correct\": false},\n" +
                                                    "        {\"text\": \"Opción D\", \"correct\": false}\n" +
                                                    "      ]\n" +
                                                    "    }\n" +
                                                    "  ]\n" +
                                                    "}\n\n" +
                                                    "Contenido educativo:\n%s", numberOfQuestions, storyContent))
                            }
                    )
            });

            String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + apiKey;
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            String responseBody = response.getBody();
            System.out.println("Respuesta de Gemini: " + responseBody);

            if (responseBody != null && responseBody.contains("\"text\":")) {
                String pattern = "```json\\s*(.*?)\\s*```";
                Pattern regex = Pattern.compile(pattern, Pattern.DOTALL);
                Matcher matcher = regex.matcher(responseBody);

                if (matcher.find()) {
                    String extractedText = matcher.group(1);
                    extractedText = extractedText.replace("\\n", "\n").replace("\\\"", "\"");
                    System.out.println("Texto extraído con regex: " + extractedText);
                    return extractedText;
                }
            }

            System.out.println("No se pudo extraer texto de la respuesta");
            return responseBody;
        } catch (Exception e) {
            System.err.println("Error completo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando preguntas con Gemini: " + e.getMessage(), e);
        }
    }
}