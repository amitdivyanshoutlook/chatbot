# Career Guidance Feature

## Overview
The Career Guidance feature helps students discover career paths based on their current qualifications, interests, and preferred fields. It uses AI-powered recommendations through the Perplexity API to provide comprehensive career guidance including educational paths, professional opportunities, and best institutions.

## Features

### 🎯 Core Functionality
- **Comprehensive Qualification Search**: Searchable dropdown with 100+ qualifications from around the world
- **AI-powered Guidance**: Uses Perplexity API for real-time, comprehensive career recommendations
- **Institution Recommendations**: Get suggestions for best institutions and universities
- **Interest-based Recommendations**: Personalized suggestions based on user interests
- **Field-specific Advice**: Targeted guidance for preferred career fields
- **Multi-language Support**: Available in both Hindi and English
- **Modern UI**: Attractive, mobile-friendly interface with smooth animations

### 🔧 Technical Features
- **Real-time Search**: Instant qualification search with keyboard navigation
- **API-driven**: All career guidance comes from Perplexity API for up-to-date information
- **Usage Tracking**: Integrated with existing daily usage limits
- **Responsive Design**: Modern, mobile-first interface with smooth animations
- **Session Management**: Secure user authentication
- **Error Handling**: Graceful error handling with user-friendly messages

## How to Use

### 1. Access Career Guidance
- Login to your account
- Click on your profile dropdown in the top-right corner
- Select "Career Guidance" (marked with "New" badge)

### 2. Get Personalized Guidance
- **Search Qualification**: Start typing your qualification in the search box
- **Select from Dropdown**: Choose from 100+ available qualifications or select "Other"
- **Add Details**: Select preferred language (Hindi/English)
- **Optional Information**: Add your interests and preferred field for more targeted advice
- **Get Guidance**: Click "Get My Career Guidance" for comprehensive recommendations

### 3. Search Features
- **Real-time Search**: Type to filter qualifications instantly
- **Keyboard Navigation**: Use arrow keys to navigate, Enter to select
- **Comprehensive List**: Includes qualifications from school level to doctoral degrees
- **International Options**: Covers both Indian and international qualifications

## API Endpoints

### Career Guidance Endpoints
```
POST /api/career/guidance
- Get career guidance with custom input
- Body: { qualification, language, interests, preferredField }

GET /api/career/qualifications
- Get comprehensive list of 100+ qualifications worldwide

POST /api/career/guidance/system
- System-level guidance (no user context required)
```

## Database Schema

### CareerOption Entity
```sql
CREATE TABLE career_options (
    id BIGINT IDENTITY PRIMARY KEY,
    qualification VARCHAR(255) NOT NULL,
    career_path VARCHAR(1000) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    category VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    duration_type VARCHAR(255) NOT NULL,
    prerequisites VARCHAR(1000),
    career_prospects VARCHAR(1000)
);
```

## Sample Career Options

The system includes predefined career options for:

### 10th Standard
- Science Stream (11th-12th)
- Commerce Stream (11th-12th)
- Arts/Humanities Stream (11th-12th)
- ITI (Industrial Training Institute)
- Polytechnic Diploma

### 12th Science
- B.Tech/B.E. (Engineering)
- MBBS (Medical)
- B.Sc. (Bachelor of Science)
- BCA (Bachelor of Computer Applications)

### 12th Commerce
- B.Com (Bachelor of Commerce)
- CA (Chartered Accountancy)
- BBA (Bachelor of Business Administration)

### B.Tech
- M.Tech (Master of Technology)
- MBA (Master of Business Administration)
- Software Industry Jobs

### BCA
- MCA (Master of Computer Applications)
- Software Development Jobs

### MBA
- Corporate Management Roles
- Consulting
- Entrepreneurship

## Configuration

### Application Properties
```properties
# Perplexity API configuration is already set up
# No additional configuration needed for career guidance
```

### Dependencies
The feature uses existing dependencies:
- Spring Boot JPA
- Hibernate
- Perplexity API integration
- Existing user management system

## Usage Limits
- Career guidance requests count towards daily usage limits
- Same limits as regular chat functionality
- Users get notified when approaching limit

## Language Support

### Hindi
- Complete interface in Hindi
- AI responses in Devanagari script
- Cultural context in recommendations

### English
- Professional English interface
- International career perspectives
- Global opportunities focus

## Error Handling
- Network timeout handling with retry mechanism
- Graceful fallback for API failures
- User-friendly error messages
- Session validation

## Future Enhancements

### Planned Features
1. **Career Assessment Quiz**: Interactive questionnaire for better recommendations
2. **Industry Trends**: Real-time job market data integration
3. **Skill Gap Analysis**: Identify skills needed for target careers
4. **Course Recommendations**: Suggest specific courses and certifications
5. **Mentor Connect**: Connect with professionals in desired fields
6. **Career Roadmap**: Step-by-step career progression plans

### Technical Improvements
1. **Caching**: Cache frequently requested career data
2. **Analytics**: Track popular career paths and user preferences
3. **Machine Learning**: Improve recommendations based on user feedback
4. **External APIs**: Integration with job portals and course providers

## Support
For technical issues or feature requests related to Career Guidance:
1. Check application logs for API call status
2. Verify database connectivity
3. Ensure Perplexity API key is valid
4. Contact system administrator for configuration issues

## Contributing
To add new career options:
1. Update `DataInitializationService.java`
2. Add new `CareerOption` entries
3. Test with different qualification inputs
4. Verify AI responses are relevant

---

**Note**: This feature integrates seamlessly with the existing Perplexity educational platform and maintains all security and usage tracking features.