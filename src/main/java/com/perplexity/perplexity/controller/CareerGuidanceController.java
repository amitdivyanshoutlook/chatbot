package com.perplexity.perplexity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.perplexity.perplexity.model.CareerGuidanceRequest;
import com.perplexity.perplexity.model.User;
import com.perplexity.perplexity.service.CareerGuidanceService;
import com.perplexity.perplexity.service.UsageService;
import com.perplexity.perplexity.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/career")
public class CareerGuidanceController {

    @Autowired
    private CareerGuidanceService careerGuidanceService;
    
    @Autowired
    private UsageService usageService;

    @Autowired
    private UserService userService;

    @PostMapping("/guidance")
    public ResponseEntity<String> getCareerGuidance(@RequestBody CareerGuidanceRequest request, HttpSession session) throws JsonProcessingException {
        System.out.println("=== CAREER GUIDANCE REQUEST ===");
        System.out.println("Qualification: " + request.getQualification());
        System.out.println("Language: " + request.getLanguage());
        System.out.println("Interests: " + request.getInterests());
        System.out.println("Preferred Field: " + request.getPreferredField());
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Please login to continue");
        }
        
        // Check daily usage limit
        if (!usageService.canMakeRequest(userId)) {
            return ResponseEntity.status(429).body("Your daily usage limit is exhausted. Please try again tomorrow.");
        }
        
        // Increment usage count
        usageService.incrementUsage(userId);
        User user = userService.getUserById(userId);
        
        // Get language preference (default to Hindi if not specified)
        String language = request.getLanguage() != null ? request.getLanguage() : "hindi";
        
        // Get career guidance
        String response = careerGuidanceService.getCareerGuidance(
            request.getQualification(), 
            language, 
            request.getInterests(), 
            request.getPreferredField(), 
            user
        );
        
        // Add remaining requests info based on language
        int remaining = usageService.getRemainingRequests(userId);
        if (remaining <= 3) {
            if ("english".equalsIgnoreCase(language)) {
                response += "\n\n[You have " + remaining + " questions remaining today]";
            } else {
                response += "\n\n[आपके पास आज " + remaining + " प्रश्न बचे हैं]";
            }
        }
        
        return ResponseEntity.ok(response);
    }



    @GetMapping("/qualifications")
    public ResponseEntity<List<String>> getAllQualifications() {
        List<String> qualifications = getComprehensiveQualificationsList();
        return ResponseEntity.ok(qualifications);
    }
    
    private List<String> getComprehensiveQualificationsList() {
        return Arrays.asList(
                // Pre-Primary/Foundation
                "Kindergarten", "Pre-school Certificate", "Pre-primary Diploma", "Entry-Level Certificate", "Entry-Level Diploma",

                // Secondary Education
                "10th Standard/Grade", "Secondary School Certificate (SSC)", "O-Levels", "GCSE", "A-Levels", "High School Diploma",
                "International Baccalaureate (IB Diploma)", "Matriculation", "Senior Secondary Certificate (HSC)", "General Certificate of Education",
                // BSc Streams
                "BSc Physics", "BSc Chemistry", "BSc Mathematics", "BSc Computer Science", "BSc Information Technology",
                "BSc Data Science", "BSc Artificial Intelligence", "BSc Cybersecurity", "BSc Cloud Computing",
                "BSc Biology", "BSc Biotechnology", "BSc Microbiology", "BSc Biochemistry", "BSc Zoology", "BSc Botany",
                "BSc Genetics", "BSc Environmental Science", "BSc Geology", "BSc Forensic Science",
                "BSc Agriculture", "BSc Forestry", "BSc Dairy Technology", "BSc Food Technology", "BSc Aeronautical Science",
                "BSc Nutrition & Dietetics", "BSc Fashion Design", "BSc Interior Design", "BSc Animation",
                "BSc Aquaculture", "BSc Aviation", "BSc Medical Technology", "BSc Nautical Science",
                "BSc Business Analytics", "BSc Urban Planning", "BSc Actuarial Science", "BSc Social Work",
                "BSc Real Estate Management", "BSc Public Administration", "BSc Mass Communication",
                "BSc Digital Media", "BSc Multimedia", "BSc E-Commerce", "BSc Economics",

                // MSc Streams
                "MSc Computer Science", "MSc Data Science", "MSc Cyber Security", "MSc Information Systems",
                "MSc Health Informatics", "MSc Nursing", "MSc Bioinformatics", "MSc Environmental Science",
                "MSc Statistics", "MSc Organic Chemistry", "MSc Geography", "MSc Psychology", "MSc Geology",
                "MSc Analytical Chemistry", "MSc Electronics", "MSc Home Science", "MSc Food Technology",
                "MSc Fashion Designing", "MSc Economics", "MSc Applied Mathematics", "MSc Clinical Research",
                "MSc Entomology", "MSc Soil Science", "MSc Anthropology", "MSc Visual Communication",
                "MSc Neuroscience", "MSc Biomedical Science", "MSc Software Engineering",
                "MRes", "MPhil", "MSt", "MLis", "MPA", "MPP", "MSW", "LLM", "MFA", "MMus",

                // MCom Streams
                "MCom Accounting", "MCom Finance", "MCom Banking", "MCom Taxation",
                "MCom International Business", "MCom Business Analytics", "MCom Marketing Management",
                "MCom General",

                // Post-Secondary Non-Tertiary
                "Post-Secondary Non-Tertiary Certificate",

                // Diploma & Early Tertiary
                "ITI Certificate", "Certificate I/II/III/IV", "Polytechnic Diploma", "Advanced Diploma", "Diploma Course",
                "Diploma in Engineering", "Diploma in Computer Science", "HNC", "HND", "Diploma of Higher Education", "Foundation Degree",

                // Associate Degrees
                "Associate Degree",

                // Undergraduate/Bachelor’s
                "B.Tech", "B.E.", "B.Sc", "B.Com", "B.A.", "BCA", "BBA", "B.Arch", "B.Des", "B.Pharm", "BA (Hons)", "BSc (Hons)",
                // Specialized
                "B.Tech CSE/IT/Mechanical/Civil/Electrical/Electronics", "B.Sc Computer Science/IT/Physics/Chemistry/Maths/Biology/Biotech/Microbiology",
                "B.Com (Accounting/Finance/Banking/Taxation)", "B.A. (English/History/PolSci/Psych/Economics)", "BFA", "BSW",
                "Bachelor of Journalism", "Mass Communication", "Hotel Management", "Fashion Design",
                "MBBS", "BDS", "BAMS", "BHMS", "BUMS", "Pharm.D", "B.Sc Nursing", "BPT", "BOT",
                "BVSc", "BPT", "OT", "Bachelor of Occupational Therapy / Physiotherapy / Veterinary Science",
                "International Bachelor’s", "Trade Certificate", "Vocational Training", "Skill Development Course",

                // Graduate Certificates/Diplomas
                "Graduate Certificate", "Graduate Diploma", "Postgraduate Certificate", "Postgraduate Diploma", "Advanced Master's",

                // Postgraduate (Master’s)
                "M.Tech", "M.E.", "M.Sc", "M.Com", "M.A.", "MCA", "MBA", "M.Arch", "M.Des", "M.Pharm", "MCA",
                "MSc Data Science/AI/Cybersecurity/Biotech", "MBA Finance/Marketing/HR/Operations/International Business",
                "MA (English/History/Psych/Economics/PolSci)", "MFA", "MPhil", "MRes", "MLitt", "MPA", "MPH", "MSW", "MSN",

                // Professional Courses
                "CA", "CS", "CMA", "CFA", "FRM", "ACCA", "CPA", "LLB", "LLM", "B.Ed", "M.Ed", "D.Ed", "Diploma in Education",
                "PMP", "CISSP", "ITIL", "Vocational License", "Trade Apprenticeship",

                // Doctoral & Professional Doctorates
                "Ph.D", "Professional Doctorate", "Doc of Business Admin (DBA)", "EdD", "MD", "MS", "DM", "MCh", "MDS",
                "DClinPsy", "DPharm", "DDiv", "JD", "DVM", "DEng", "DTech", "DSc", "DLitt", "DCL", "DMedSc",
                "Doctor of Arts (DA)", "Doctor of Theology (ThD)", "Doctor of Ministry (DMin)", "Doctor of Public Health (DrPH)",
                "Doctor of Veterinary Medicine (DVM)",

                // Higher Doctorates
                "Higher Doctorate (e.g. DSc, DLitt)",

                // International Qualifications
                "Associate Degree (International)", "Graduate Certificate (International)", "Postgraduate Diploma (International)",
                "Short-Cycle Tertiary", "Professional Certificate", "Skill Development Course",

                // Creative & Arts
                "Bachelor of Fine Arts", "Master of Fine Arts", "Diploma in Fine Arts", "Certificate in Arts",
                "Bachelor of Music", "Master of Music", "Diploma in Music", "Certificate in Music",
                "Bachelor/Master of Dance", "Diploma/Certificate in Dance",

                // Other Specialized Fields
                "Aviation Course", "Pilot Training", "Air Traffic Control", "Maritime Studies", "Merchant Navy",
                "Hotel Management", "Tourism Management", "Event Management", "Sports Management",
                "Agriculture", "Forestry", "Fisheries", "Dairy Technology", "Food Technology",

                "Other"
        );
    }

/*    @PostMapping("/guidance/system")
    public ResponseEntity<String> getSystemCareerGuidance(@RequestBody CareerGuidanceRequest request) throws JsonProcessingException {
        System.out.println("=== SYSTEM CAREER GUIDANCE REQUEST ===");
        System.out.println("Qualification: " + request.getQualification());
        System.out.println("Language: " + request.getLanguage());
        
        // Get language preference (default to Hindi if not specified)
        String language = request.getLanguage() != null ? request.getLanguage() : "hindi";
        
        // Get career guidance without user context
        String response = careerGuidanceService.getCareerGuidanceForSystem(
            request.getQualification(), 
            language, 
            request.getInterests(), 
            request.getPreferredField()
        );
        
        return ResponseEntity.ok(response);
    }*/
}