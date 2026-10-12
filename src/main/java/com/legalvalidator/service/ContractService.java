package com.legalvalidator.service;

import com.legalvalidator.model.Contract;
import com.legalvalidator.model.Issue;
import com.legalvalidator.model.ValidationResult;
import com.legalvalidator.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ContractService {

    @Autowired private PDFParserService pdfParserService;
    @Autowired private DroolsRuleService droolsRuleService;
    @Autowired private AIService aiService;
    @Autowired private ContractRepository contractRepository;

    public ValidationResult validateContract(MultipartFile file) {
        long startTime = System.currentTimeMillis();
        ValidationResult result = new ValidationResult();

        try {
            Contract contract = new Contract();
            contract.setFileName(file.getOriginalFilename());
            contract.setFileType(file.getContentType());
            contract.setFileSize(file.getSize());

            String contractText = pdfParserService.extractTextFromPDF(file);
            contract.setExtractedText(contractText);

            List<Issue> droolsIssues = droolsRuleService.validateWithDrools(contractText);
            result.setDroolsIssues(droolsIssues);

            List<Issue> aiIssues = aiService.getAISuggestions(contractText);
            result.setAiIssues(aiIssues);

            String aiSummary = aiService.analyzeContract(contractText);
            result.setSummary(aiSummary);

            int riskScore = calculateRiskScore(droolsIssues, aiIssues);
            result.setRiskScore(riskScore);

            contract.setRiskScore(riskScore);
            contract.setStatus("COMPLETED");

            // DB save — ব্যর্থ হলেও পুরো validation fail হবে না
            try {
                Contract savedContract = contractRepository.save(contract);
                result.setContractId(savedContract.getId());
            } catch (Exception dbEx) {
                System.err.println("⚠️ DB save failed (continuing): " + dbEx.getMessage());
            }

            result.setFileName(file.getOriginalFilename());

        } catch (Exception e) {
            System.err.println("❌ Validation failed: " + e.getMessage());
            e.printStackTrace();
            result.setSummary("বিশ্লেষণ ব্যর্থ হয়েছে: " + e.getMessage());
            result.setRiskScore(0);
        }

        result.setProcessingTimeMs(System.currentTimeMillis() - startTime);
        return result;
    }

    private int calculateRiskScore(List<Issue> droolsIssues, List<Issue> aiIssues) {
        int score = 0;
        for (Issue issue : droolsIssues) {
            switch (issue.getSeverity() == null ? "" : issue.getSeverity()) {
                case "CRITICAL" -> score += 30;
                case "HIGH"     -> score += 20;
                case "MEDIUM"   -> score += 10;
                case "LOW"      -> score += 5;
            }
        }
        for (Issue issue : aiIssues) {
            switch (issue.getSeverity() == null ? "" : issue.getSeverity()) {
                case "HIGH"   -> score += 15;
                case "MEDIUM" -> score += 8;
            }
        }
        return Math.min(score, 100);
    }
}