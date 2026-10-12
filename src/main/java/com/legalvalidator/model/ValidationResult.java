package com.legalvalidator.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private Long contractId;
    private String fileName;
    private int riskScore;
    private String riskLevel;
    private List<Issue> droolsIssues = new ArrayList<>();
    private List<Issue> aiIssues = new ArrayList<>();
    private List<String> aiSuggestions = new ArrayList<>();
    private String summary;
    private long processingTimeMs;
    
    // Constructors
    public ValidationResult() {}
    
    // Getters
    public Long getContractId() {
        return contractId;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public int getRiskScore() {
        return riskScore;
    }
    
    public String getRiskLevel() {
        return riskLevel;
    }
    
    public List<Issue> getDroolsIssues() {
        return droolsIssues;
    }
    
    public List<Issue> getAiIssues() {
        return aiIssues;
    }
    
    public List<String> getAiSuggestions() {
        return aiSuggestions;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public long getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    // Setters
    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
        calculateRiskLevel();
    }
    
    public void setDroolsIssues(List<Issue> droolsIssues) {
        this.droolsIssues = droolsIssues;
    }
    
    public void setAiIssues(List<Issue> aiIssues) {
        this.aiIssues = aiIssues;
    }
    
    public void setAiSuggestions(List<String> aiSuggestions) {
        this.aiSuggestions = aiSuggestions;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
    
    // Business Methods
    public List<Issue> getAllIssues() {
        List<Issue> all = new ArrayList<>();
        all.addAll(droolsIssues);
        all.addAll(aiIssues);
        return all;
    }
    
    private void calculateRiskLevel() {
        if (riskScore >= 75) {
            riskLevel = "CRITICAL";
        } else if (riskScore >= 50) {
            riskLevel = "HIGH";
        } else if (riskScore >= 25) {
            riskLevel = "MEDIUM";
        } else {
            riskLevel = "LOW";
        }
    }
}