package com.perplexity.perplexity.model;

public class CareerGuidanceRequest {
    private String qualification;
    private String language;
    private String interests; // Optional field for user interests
    private String preferredField; // Optional field for preferred field

    public CareerGuidanceRequest() {}

    public CareerGuidanceRequest(String qualification, String language, String interests, String preferredField) {
        this.qualification = qualification;
        this.language = language;
        this.interests = interests;
        this.preferredField = preferredField;
    }

    // Getters and Setters
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getInterests() { return interests; }
    public void setInterests(String interests) { this.interests = interests; }
    
    public String getPreferredField() { return preferredField; }
    public void setPreferredField(String preferredField) { this.preferredField = preferredField; }
}