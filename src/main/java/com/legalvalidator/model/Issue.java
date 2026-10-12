package com.legalvalidator.model;

public class Issue {
    private String type;
    private String severity;
    private String description;
    private String suggestedFix;
    private String clauseLocation;
    
    // হাইলাইটিং এর জন্য নতুন দুটি ফিল্ড
    private int startIndex;
    private int endIndex;
    
    // Default Constructor (Drools এবং Jackson এর জন্য প্রয়োজন)
    public Issue() {}
    
    // ৩ প্যারামিটার বিশিষ্ট কনস্ট্রাক্টর
    public Issue(String type, String severity, String description) {
        this.type = type;
        this.severity = severity;
        this.description = description;
    }
    
    // ৪ প্যারামিটার বিশিষ্ট কনস্ট্রাক্টর
    public Issue(String type, String severity, String description, String suggestedFix) {
        this.type = type;
        this.severity = severity;
        this.description = description;
        this.suggestedFix = suggestedFix;
    }

    // ৬ প্যারামিটার বিশিষ্ট কনস্ট্রাক্টর (হাইলাইটিং এর জন্য ড্রুলসে এটি ব্যবহার করবেন)
    public Issue(String type, String severity, String description, String suggestedFix, int startIndex, int endIndex) {
        this.type = type;
        this.severity = severity;
        this.description = description;
        this.suggestedFix = suggestedFix;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
    
    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSuggestedFix() { return suggestedFix; }
    public void setSuggestedFix(String suggestedFix) { this.suggestedFix = suggestedFix; }

    public String getClauseLocation() { return clauseLocation; }
    public void setClauseLocation(String clauseLocation) { this.clauseLocation = clauseLocation; }

    public int getStartIndex() { return startIndex; }
    public void setStartIndex(int startIndex) { this.startIndex = startIndex; }

    public int getEndIndex() { return endIndex; }
    public void setEndIndex(int endIndex) { this.endIndex = endIndex; }
}