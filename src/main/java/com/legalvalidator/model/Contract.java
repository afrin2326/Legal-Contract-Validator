package com.legalvalidator.model;

import java.time.LocalDateTime;

public class Contract {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String extractedText;
    private Integer riskScore;
    private String status;
    private LocalDateTime uploadedAt;
    
    // Constructors
    public Contract() {
        this.uploadedAt = LocalDateTime.now();
        this.status = "PENDING";
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public String getExtractedText() {
        return extractedText;
    }
    
    public Integer getRiskScore() {
        return riskScore;
    }
    
    public String getStatus() {
        return status;
    }
    
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }
    
    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}