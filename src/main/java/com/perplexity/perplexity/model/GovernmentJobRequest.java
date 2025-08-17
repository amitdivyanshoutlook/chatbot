package com.perplexity.perplexity.model;

public class GovernmentJobRequest {
    private String qualification;
    private String fieldOfStudy;
    private int age;
    private String location;
    private String jobType;

    // Default constructor
    public GovernmentJobRequest() {}

    // Constructor with parameters
    public GovernmentJobRequest(String qualification, String fieldOfStudy, int age, String location, String jobType) {
        this.qualification = qualification;
        this.fieldOfStudy = fieldOfStudy;
        this.age = age;
        this.location = location;
        this.jobType = jobType;
    }

    // Getters and setters
    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public String toString() {
        return "GovernmentJobRequest{" +
                "qualification='" + qualification + '\'' +
                ", fieldOfStudy='" + fieldOfStudy + '\'' +
                ", age=" + age +
                ", location='" + location + '\'' +
                ", jobType='" + jobType + '\'' +
                '}';
    }
}
