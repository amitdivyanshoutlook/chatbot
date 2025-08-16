# Career Guidance Timeout Optimizations

## Overview
This document outlines the optimizations made to resolve timeout issues in the Career Guidance feature.

## 1. Timeout Settings Increased to 60 Seconds

### OkHttpClient Configuration
Updated `PerplexityService.java` with comprehensive timeout settings:
```java
private final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)    // Connection timeout
        .writeTimeout(60, TimeUnit.SECONDS)      // Write timeout
        .readTimeout(60, TimeUnit.SECONDS)       // Read timeout
        .callTimeout(60, TimeUnit.SECONDS)       // Overall call timeout
        .build();
```

### Application Properties
Added timeout configurations:
```properties
perplexity.api.timeout=60
spring.mvc.async.request-timeout=60000
```

## 2. Optimized Prompts for Faster Response

### Before (Verbose Prompt)
- 400 words maximum
- 8 detailed sections requested
- Complex instructions
- Longer processing time

### After (Optimized Prompt)
- 250-300 words maximum
- 5 focused sections
- Direct, concise instructions
- Faster processing

### English Prompt Optimization
```
Quick career guide for [qualification] with interest in [interests] focusing on [field]. 
Provide in 250 words: 
1) Top 3 career paths 
2) Best 2 institutions 
3) Key skills 
4) Salary range 
5) Next step. 
Use bullet points, be direct.
```

### Hindi Prompt Optimization
```
[qualification] के लिए त्वरित करियर गाइड, रुचि: [interests], क्षेत्र: [field]। 
250 शब्दों में दें: 
1) शीर्ष 3 करियर 
2) सर्वोत्तम 2 संस्थान 
3) मुख्य कौशल 
4) वेतन 
5) अगला कदम। 
बुलेट पॉइंट्स, सीधे जवाब।
```

## 3. Enhanced Error Handling

### Specific Timeout Exception Handling
```java
catch (java.net.SocketTimeoutException e) {
    if (attempt < maxRetries) {
        System.out.println("Timeout on attempt " + attempt + "/" + maxRetries);
        Thread.sleep(2000 * attempt); // Longer wait for timeout
        continue;
    }
    return "Request timed out after " + maxRetries + " attempts. Please try again with a simpler query.";
}
```

### Retry Strategy
- **Regular exceptions**: 1-second exponential backoff
- **Timeout exceptions**: 2-second exponential backoff
- **Maximum retries**: 3 attempts
- **User-friendly timeout messages**

## 4. UI Improvements

### Loading Indicator
- Added timeout expectation message: "This may take up to 60 seconds for comprehensive results"
- Better error messages for timeout scenarios
- Specific handling for timeout vs other errors

### Error Messages
- **Timeout**: "Request timed out. Please try with a simpler qualification or check your internet connection."
- **General Error**: Standard error message with details

## 5. Performance Optimizations

### Prompt Efficiency
1. **Reduced word count**: 400 → 250-300 words
2. **Fewer sections**: 8 → 5 key sections
3. **Simpler language**: Direct, concise instructions
4. **Focused requests**: Top 3 instead of 5-7 options

### Response Processing
- Maintained existing retry logic
- Added specific timeout handling
- Improved error categorization

## 6. Testing Recommendations

### Test Scenarios
1. **Simple qualifications**: "B.Tech", "MBA", "12th Science"
2. **Complex qualifications**: "Master of Computer Applications in Data Science"
3. **With interests**: Include interests and preferred fields
4. **Network conditions**: Test with slower internet connections

### Expected Results
- **Fast responses**: Simple qualifications should respond in 10-20 seconds
- **Medium responses**: Complex qualifications in 30-45 seconds
- **Timeout handling**: Graceful failure after 60 seconds with retry options

## 7. Monitoring

### Log Messages
- "Timeout on attempt X/3" for timeout tracking
- "API call successful on attempt X" for success tracking
- Response time monitoring through application logs

### User Experience
- Clear loading indicators
- Timeout expectations set upfront
- Helpful error messages with actionable advice

## 8. Future Improvements

### Potential Enhancements
1. **Caching**: Cache common qualification responses
2. **Progressive responses**: Stream partial results
3. **Fallback responses**: Pre-generated responses for common qualifications
4. **Load balancing**: Multiple API endpoints if available

### Monitoring Metrics
- Average response time by qualification type
- Timeout rate percentage
- User retry patterns
- Most problematic qualifications

## Summary

These optimizations should significantly reduce timeout issues while maintaining the quality of career guidance responses. The combination of increased timeout limits, optimized prompts, and better error handling provides a robust solution for users.

**Key Benefits:**
- ✅ 60-second timeout allowance
- ✅ Faster, more focused responses
- ✅ Better error handling and user feedback
- ✅ Improved retry mechanisms
- ✅ Clear user expectations