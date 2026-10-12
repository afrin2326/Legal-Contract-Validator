package com.legalvalidator.controller;

import com.legalvalidator.model.ValidationResult;
import com.legalvalidator.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
// @CrossOrigin সরানো হয়েছে — WebConfig handle করছে
public class ContractController {

    @Autowired
    private ContractService contractService;

    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateContract(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        try {
            ValidationResult result = contractService.validateContract(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error during contract validation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("backend", "Spring Boot (Java)");
        response.put("ai_engine", "FastAPI (Python)");
        response.put("llm_model", "Llama 3 & BanglaBERT");
        response.put("message", "✅ Hybrid Legal AI System is Online!");
        return ResponseEntity.ok(response);
    }
}