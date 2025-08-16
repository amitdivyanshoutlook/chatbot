package com.perplexity.perplexity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String mobileNumber;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private Integer age;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String qualification;
    
    @Column(nullable = false)
    private String password;

    public User() {}

    public User(String mobileNumber, String firstName, Integer age, String address, String qualification, String password) {
        this.mobileNumber = mobileNumber;
        this.firstName = firstName;
        this.age = age;
        this.address = address;
        this.qualification = qualification;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}