package com.legalvalidator.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

@Service
public class PDFParserService {
    
    public String extractTextFromPDF(MultipartFile file) throws Exception {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new Exception("ফাইল খালি। দয়া করে একটি বৈধ PDF ফাইল আপলোড করুন।");
        }
        
        // Check if file is PDF
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new Exception("শুধুমাত্র PDF ফাইল সাপোর্ট করা হয়।");
        }
        
        System.out.println("✅ [PDFBox 3.x] PDF parsing started for: " + fileName);
        
        // Extract text from PDF using PDFBox 3.0
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            if (text == null || text.trim().isEmpty()) {
                throw new Exception("PDF থেকে কোন টেক্সট পাওয়া যায়নি। ফাইলটি স্ক্যান করা কপি হতে পারে।");
            }
            
            System.out.println("✅ PDF Parsing successful! Extracted " + text.length() + " characters.");
            System.out.println("📄 First 200 characters: " + text.substring(0, Math.min(200, text.length())));
            
            return text;
            
        } catch (Exception e) {
            System.err.println("❌ PDF Parsing failed: " + e.getMessage());
            throw new Exception("PDF পার্সিং ব্যর্থ হয়েছে: " + e.getMessage());
        }
    }
}