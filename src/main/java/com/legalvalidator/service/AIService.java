package com.legalvalidator.service;

import com.legalvalidator.model.Issue;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

@Service
public class AIService {

    private final RestTemplate restTemplate;
    private final String PYTHON_API_URL = "http://localhost:8000/analyze-legal";

    public AIService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(3).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(15).toMillis());
        this.restTemplate = new RestTemplate(factory);
    }

    public String analyzeContract(String contractText) {
        Map<String, Object> aiResponse = callPythonAiApi(contractText);

        if (aiResponse == null) {
            return "⚠️ AI Engine is currently offline. Please check your Python server.";
        }

        StringBuilder analysis = new StringBuilder();
        analysis.append("📊 **Legal AI Engine বিশ্লেষণ রিপোর্ট**\n\n");

        String suggestion = (String) aiResponse.get("suggestion");
        analysis.append("💡 **AI আইনি পরামর্শ (Llama 3):**\n");
        analysis.append(suggestion != null ? suggestion : "কোনো সাজেশন পাওয়া যায়নি।").append("\n\n");

        try {
            @SuppressWarnings("unchecked")
            List<String> entities = (List<String>) aiResponse.get("entities");
            if (entities != null && !entities.isEmpty()) {
                analysis.append("🔍 **শনাক্তকৃত গুরুত্বপূর্ণ তথ্য (BanglaBERT):**\n");
                for (String entity : entities) {
                    analysis.append("• ").append(entity).append("\n");
                }
            }
        } catch (Exception e) {
            analysis.append("🔍 (এনটিটি এক্সট্রাকশনে কিছু সমস্যা হয়েছে)\n");
        }

        return analysis.toString();
    }

    public List<Issue> getAISuggestions(String contractText) {
        List<Issue> issues = new ArrayList<>();
        Map<String, Object> aiResponse = callPythonAiApi(contractText);

        if (aiResponse != null) {
            String suggestion = (String) aiResponse.get("suggestion");
            if (suggestion != null && !suggestion.isBlank()) {
                issues.add(new Issue("AI_SUGGESTION", "INFO", "AI Recommendation", suggestion));
            }
        }
        return issues;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<String, Object> callPythonAiApi(String text) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    PYTHON_API_URL, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("⚠️ Python AI Engine not reachable: " + e.getMessage());
            return null;
        }
    }
}