package com.perplexity.perplexity.service;

import com.perplexity.perplexity.model.CareerOption;
import com.perplexity.perplexity.repository.CareerOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private CareerOptionRepository careerOptionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize career options if database is empty
        if (careerOptionRepository.count() == 0) {
            initializeCareerOptions();
        }
    }

    private void initializeCareerOptions() {
        List<CareerOption> careerOptions = Arrays.asList(
            // 10th Standard Options
            new CareerOption("10th", "Science Stream (11th-12th)", 
                "Continue with Physics, Chemistry, Mathematics/Biology for engineering or medical careers", 
                "Academic", 2, "years", 
                "Good marks in 10th, interest in science subjects", 
                "Engineering, Medical, Research, Teaching"),
            
            new CareerOption("10th", "Commerce Stream (11th-12th)", 
                "Study Business, Economics, Accountancy for commerce and management careers", 
                "Academic", 2, "years", 
                "Interest in business, mathematics, economics", 
                "CA, CS, Banking, Business Management, Economics"),
            
            new CareerOption("10th", "Arts/Humanities Stream (11th-12th)", 
                "Study Literature, History, Political Science, Psychology for diverse career paths", 
                "Academic", 2, "years", 
                "Interest in languages, social sciences, creative fields", 
                "Civil Services, Journalism, Teaching, Law, Psychology"),
            
            new CareerOption("10th", "ITI (Industrial Training Institute)", 
                "Technical training in various trades like electrician, mechanic, computer operator", 
                "Skill-based", 1, "years", 
                "Interest in hands-on technical work", 
                "Technician jobs, self-employment, government jobs"),
            
            new CareerOption("10th", "Polytechnic Diploma", 
                "3-year diploma in engineering fields like mechanical, electrical, civil, computer science", 
                "Technical", 3, "years", 
                "Interest in engineering and technology", 
                "Junior Engineer, Technician, Supervisor roles"),
            
            // 12th Science Options
            new CareerOption("12th Science", "B.Tech/B.E. (Engineering)", 
                "4-year undergraduate degree in various engineering disciplines", 
                "Academic", 4, "years", 
                "JEE Main/Advanced, good marks in PCM", 
                "Software Engineer, Mechanical Engineer, Civil Engineer, Research"),
            
            new CareerOption("12th Science", "MBBS (Medical)", 
                "5.5-year medical degree to become a doctor", 
                "Academic", 6, "years", 
                "NEET qualification, good marks in PCB", 
                "Doctor, Surgeon, Medical Researcher, Healthcare"),
            
            new CareerOption("12th Science", "B.Sc. (Bachelor of Science)", 
                "3-year degree in Physics, Chemistry, Mathematics, Biology, Computer Science", 
                "Academic", 3, "years", 
                "Good marks in science subjects", 
                "Research, Teaching, Lab Technician, Further Studies"),
            
            new CareerOption("12th Science", "BCA (Bachelor of Computer Applications)", 
                "3-year degree focused on computer applications and programming", 
                "Technical", 3, "years", 
                "Interest in computers and programming", 
                "Software Developer, System Analyst, Web Developer"),
            
            // 12th Commerce Options
            new CareerOption("12th Commerce", "B.Com (Bachelor of Commerce)", 
                "3-year degree in commerce, accounting, and business studies", 
                "Academic", 3, "years", 
                "Good marks in commerce subjects", 
                "Accountant, Banking, Finance, Business Analyst"),
            
            new CareerOption("12th Commerce", "CA (Chartered Accountancy)", 
                "Professional course in accounting and auditing", 
                "Professional", 4, "years", 
                "Strong mathematical skills, attention to detail", 
                "Chartered Accountant, Tax Consultant, Financial Advisor"),
            
            new CareerOption("12th Commerce", "BBA (Bachelor of Business Administration)", 
                "3-year management degree covering all aspects of business", 
                "Academic", 3, "years", 
                "Leadership skills, interest in business", 
                "Management roles, Entrepreneur, Business Analyst"),
            
            // B.Tech Options
            new CareerOption("B.Tech", "M.Tech (Master of Technology)", 
                "2-year postgraduate degree for specialization in engineering", 
                "Academic", 2, "years", 
                "Good B.Tech grades, GATE qualification", 
                "Senior Engineer, Research, Teaching, Technical Lead"),
            
            new CareerOption("B.Tech", "MBA (Master of Business Administration)", 
                "2-year management degree for leadership roles", 
                "Academic", 2, "years", 
                "CAT/MAT/XAT scores, work experience preferred", 
                "Manager, Consultant, Business Leader, Entrepreneur"),
            
            new CareerOption("B.Tech", "Software Industry Jobs", 
                "Direct employment in IT companies as software engineer", 
                "Professional", 0, "immediate", 
                "Programming skills, good academic record", 
                "Software Engineer, Full Stack Developer, DevOps Engineer"),
            
            // BCA Options
            new CareerOption("BCA", "MCA (Master of Computer Applications)", 
                "2-year postgraduate degree in computer applications", 
                "Academic", 2, "years", 
                "Good BCA grades, programming knowledge", 
                "Senior Developer, System Architect, IT Manager"),
            
            new CareerOption("BCA", "Software Development Jobs", 
                "Entry-level positions in software companies", 
                "Professional", 0, "immediate", 
                "Programming skills, project portfolio", 
                "Junior Developer, Web Developer, Mobile App Developer"),
            
            // MBA Options
            new CareerOption("MBA", "Corporate Management Roles", 
                "Management positions in various industries", 
                "Professional", 0, "immediate", 
                "Leadership skills, domain knowledge", 
                "Manager, Director, VP, Business Head"),
            
            new CareerOption("MBA", "Consulting", 
                "Management consulting roles in top consulting firms", 
                "Professional", 0, "immediate", 
                "Analytical skills, problem-solving ability", 
                "Management Consultant, Strategy Consultant, Business Analyst"),
            
            new CareerOption("MBA", "Entrepreneurship", 
                "Start your own business or startup", 
                "Entrepreneurial", 0, "immediate", 
                "Business idea, funding, risk-taking ability", 
                "Founder, CEO, Business Owner")
        );

        careerOptionRepository.saveAll(careerOptions);
        System.out.println("Initialized " + careerOptions.size() + " career options in the database");
    }
}