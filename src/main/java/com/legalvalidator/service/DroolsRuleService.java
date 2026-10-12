package com.legalvalidator.service;

import com.legalvalidator.model.Issue;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DroolsRuleService {
    
    @Autowired(required = false)  // required=false মানে KieContainer না থাকলেও error হবে না
    private KieContainer kieContainer;
    
    public List<Issue> validateWithDrools(String contractText) {
        List<Issue> issues = new ArrayList<>();
        
        // Check if KieContainer is available
        if (kieContainer == null) {
            System.out.println("⚠️ Drools KieContainer not available. Using fallback validation.");
            return getFallbackIssues(contractText);
        }
        
        KieSession kieSession = null;
        try {
            kieSession = kieContainer.newKieSession();
            kieSession.setGlobal("issues", issues);
            kieSession.insert(contractText);
            kieSession.fireAllRules();
            
            System.out.println("✅ Drools validation completed. Found " + issues.size() + " issues.");
            
        } catch (Exception e) {
            System.out.println("❌ Drools validation failed: " + e.getMessage());
            System.out.println("⚠️ Using fallback validation.");
            return getFallbackIssues(contractText);
        } finally {
            if (kieSession != null) {
                kieSession.dispose();
            }
        }
        
        return issues;
    }
    
    // Fallback validation - কাজ করবে এমনকি Drools না থাকলেও
    private List<Issue> getFallbackIssues(String text) {
        List<Issue> issues = new ArrayList<>();
        String lowerText = text.toLowerCase();
        
        // Confidentiality check
        if (!lowerText.contains("confidential") && !lowerText.contains("ndag") && 
            !lowerText.contains("গোপনীয়তা")) {
            issues.add(new Issue("MISSING_CLAUSE", "HIGH", 
                "গোপনীয়তা চুক্তি (NDA/Confidentiality) ক্লজটি অনুপস্থিত",
                "একটি গোপনীয়তা ক্লজ যোগ করুন"));
        }
        
        // Termination check
        if (!lowerText.contains("termination") && !lowerText.contains("cancel") && 
            !lowerText.contains("বাতিল")) {
            issues.add(new Issue("MISSING_CLAUSE", "HIGH", 
                "চুক্তি বাতিলের শর্তাবলী উল্লেখ নেই",
                "৩০ দিনের নোটিশে বাতিলের শর্ত যোগ করুন"));
        }
        
        // Penalty check
        if (lowerText.contains("penalty") || lowerText.contains("জরিমানা")) {
            if (lowerText.contains("25") || lowerText.contains("৩০") || 
                lowerText.contains("৫০") || lowerText.contains("100")) {
                issues.add(new Issue("RISK_CLAUSE", "CRITICAL", 
                    "পেনাল্টির হার খুব বেশি (২৫% এর বেশি) - যা আইনত চ্যালেঞ্জযোগ্য",
                    "পেনাল্টির হার ১০-১৫% এ নামিয়ে আনুন"));
            }
        }
        
        // Dispute resolution check
        if (!lowerText.contains("dispute") && !lowerText.contains("arbitration") && 
            !lowerText.contains("বিরোধ")) {
            issues.add(new Issue("MISSING_CLAUSE", "MEDIUM", 
                "বিরোধ নিষ্পত্তির পদ্ধতি উল্লেখ নেই",
                "মধ্যস্থতা বা সালিশি ক্লজ যোগ করুন"));
        }
        
        // Governing law check
        if (!lowerText.contains("governing") && !lowerText.contains("jurisdiction") && 
            !lowerText.contains("আইন")) {
            issues.add(new Issue("MISSING_CLAUSE", "HIGH", 
                "প্রযোজ্য আইন ও এখতিয়ার উল্লেখ নেই",
                "প্রযোজ্য আইন (যেমন বাংলাদেশের আইন) এবং এখতিয়ার উল্লেখ করুন"));
        }
        
        // Force majeure check
        if (!lowerText.contains("force majeure") && !lowerText.contains("act of god") && 
            !lowerText.contains("অপ্রত্যাশিত")) {
            issues.add(new Issue("MISSING_CLAUSE", "MEDIUM", 
                "ফোর্স মেজেউর (Force Majeure) ক্লজ নেই",
                "অপ্রত্যাশিত ঘটনার জন্য ক্লজ যোগ করুন"));
        }
        
        System.out.println("✅ Fallback validation completed. Found " + issues.size() + " issues.");
        return issues;
    }
}