package com.perplexity.perplexity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "career_options")
public class CareerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String qualification;
    
    @Column(nullable = false, length = 1000)
    private String careerPath;
    
    @Column(nullable = false, length = 2000)
    private String description;
    
    @Column(nullable = false)
    private String category; // Academic, Professional, Skill-based, etc.
    
    @Column(nullable = false)
    private Integer duration; // Duration in years/months
    
    @Column(nullable = false)
    private String durationType; // years, months
    
    @Column(length = 1000)
    private String prerequisites;
    
    @Column(length = 1000)
    private String careerProspects;

    public CareerOption() {}

    public CareerOption(String qualification, String careerPath, String description, 
                       String category, Integer duration, String durationType, 
                       String prerequisites, String careerProspects) {
        this.qualification = qualification;
        this.careerPath = careerPath;
        this.description = description;
        this.category = category;
        this.duration = duration;
        this.durationType = durationType;
        this.prerequisites = prerequisites;
        this.careerProspects = careerProspects;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public String getCareerPath() { return careerPath; }
    public void setCareerPath(String careerPath) { this.careerPath = careerPath; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    public String getDurationType() { return durationType; }
    public void setDurationType(String durationType) { this.durationType = durationType; }
    
    public String getPrerequisites() { return prerequisites; }
    public void setPrerequisites(String prerequisites) { this.prerequisites = prerequisites; }
    
    public String getCareerProspects() { return careerProspects; }
    public void setCareerProspects(String careerProspects) { this.careerProspects = careerProspects; }
}