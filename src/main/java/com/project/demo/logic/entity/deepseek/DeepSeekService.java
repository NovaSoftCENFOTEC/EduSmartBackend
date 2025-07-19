package com.project.demo.logic.entity.deepseek;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DeepSeekService {

    @Value("${lmstudio.url:http://localhost:1234}")
    private String lmStudioUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateQuizQuestions(String storyContent, int numberOfQuestions) {
        try {
            System.out.println("Iniciando generación de preguntas con DeepSeek...");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");

            String prompt = "Genera " + numberOfQuestions + " preguntas de opción múltiple basadas en el siguiente contenido educativo. " +
                    "Cada pregunta debe tener 4 opciones, con solo una correcta. " +
                    "Si el contenido es muy breve o solo un título, genera preguntas basadas en el tema general mencionado. " +
                    "Asegúrate de que cada opción tenga un texto completo y significativo. " +
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
                    "Contenido educativo:\n" + storyContent;

            message.put("content", prompt);

            List<Map<String, Object>> messages = new ArrayList<>();
            messages.add(message);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.3);
            requestBody.put("max_tokens", 1500);
            requestBody.put("stream", false);

            String url = lmStudioUrl + "/v1/chat/completions";
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            String responseBody = response.getBody();
            System.out.println("Respuesta de DeepSeek: " + responseBody);

            if (responseBody != null && responseBody.contains("\"content\":")) {
                JsonNode responseJson = objectMapper.readTree(responseBody);
                String content = responseJson.path("choices").path(0).path("message").path("content").asText();

                String pattern = "```json\\s*(.*?)\\s*```";
                Pattern regex = Pattern.compile(pattern, Pattern.DOTALL);
                Matcher matcher = regex.matcher(content);

                if (matcher.find()) {
                    String extractedText = matcher.group(1);
                    extractedText = extractedText.replace("\\n", "\n").replace("\\\"", "\"");
                    System.out.println("Texto extraído con regex: " + extractedText);

                    String cleanedJson = cleanAndValidateJson(extractedText);
                    if (cleanedJson != null) {
                        return cleanedJson;
                    }
                }

                return content;
            }

            System.out.println("No se pudo extraer texto de la respuesta");
            return responseBody;
        } catch (Exception e) {
            System.err.println("Error completo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generando preguntas con DeepSeek: " + e.getMessage(), e);
        }
    }

    private String cleanAndValidateJson(String jsonText) {
        try {
            jsonText = jsonText.trim();

            jsonText = jsonText.replaceAll("\\*\\*text\\*\\*", "text");
            jsonText = jsonText.replaceAll("\\*\\*question\\*\\*", "question");

            jsonText = jsonText.replaceAll("//.*?\\n", "\n");
            jsonText = jsonText.replaceAll("/\\*.*?\\*/", "");

            jsonText = jsonText.replaceAll("\"\\s*,\\s*\"", "\", \"");
            jsonText = jsonText.replaceAll("\"\\s*,\\s*$", "\"");

            jsonText = jsonText.replaceAll("\\}\\s*\\{", "}, {");
            jsonText = jsonText.replaceAll("\\]\\s*\\{", "], {");

            jsonText = jsonText.replaceAll(",\\s*\\}", "}");
            jsonText = jsonText.replaceAll(",\\s*\\]", "]");

            jsonText = jsonText.replaceAll("\\{\"\\s*[^a-zA-Z0-9\"\\s][^\"]*\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*\"correct\"", "{\"text\": \"$1\", \"correct\"");

            jsonText = jsonText.replaceAll("\\{\"([^\"]+)\"\\s*,\\s*\"correct\"", "{\"text\": \"$1\", \"correct\"");

            jsonText = jsonText.replaceAll("\"\\s*opción incorrecta [A-Z]\"\\s*,\\s*\"correct\"", "\"text\": \"Opción alternativa\", \"correct\"");
            jsonText = jsonText.replaceAll("\"\\s*[A-Z]\"\\s*,\\s*\"correct\"", "\"text\": \"Opción alternativa\", \"correct\"");

            Pattern optionPattern = Pattern.compile("\"text\":\\s*\"([^\"]*)\"");
            Matcher optionMatcher = optionPattern.matcher(jsonText);
            StringBuffer sb = new StringBuffer();

            while (optionMatcher.find()) {
                String text = optionMatcher.group(1).trim();
                if (text.isEmpty() || text.length() < 3 || text.matches(".*[A-Z]\\s*$")) {
                    String replacement = "\"text\": \"Opción alternativa\"";
                    optionMatcher.appendReplacement(sb, replacement);
                }
            }
            optionMatcher.appendTail(sb);
            jsonText = sb.toString();

            int openBraces = 0;
            int openBrackets = 0;
            StringBuilder cleaned = new StringBuilder();

            for (char c : jsonText.toCharArray()) {
                if (c == '{') openBraces++;
                if (c == '}') openBraces--;
                if (c == '[') openBrackets++;
                if (c == ']') openBrackets--;

                cleaned.append(c);
            }

            while (openBraces > 0) {
                cleaned.append("}");
                openBraces--;
            }

            while (openBrackets > 0) {
                cleaned.append("]");
                openBrackets--;
            }

            String result = cleaned.toString();

            JsonNode jsonNode = objectMapper.readTree(result);

            JsonNode questions = jsonNode.path("questions");
            for (JsonNode question : questions) {
                JsonNode options = question.path("options");
                int correctCount = 0;
                for (JsonNode option : options) {
                    if (option.path("correct").asBoolean()) {
                        correctCount++;
                    }
                }
                if (correctCount != 1) {
                    System.out.println("Advertencia: Pregunta no tiene exactamente una opción correcta");
                }
            }

            return result;

        } catch (Exception e) {
            System.out.println("Error limpiando JSON: " + e.getMessage());
            return null;
        }
    }
}