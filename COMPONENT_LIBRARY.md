# Aadhya Eduverse - Component Library

## Overview

This document provides a comprehensive guide to all UI components available in the Aadhya Eduverse design system. Each component is designed to be accessible, responsive, and consistent with the overall design language.

## 🎨 Design Principles

### 1. Consistency
- All components follow the same design tokens
- Consistent spacing, typography, and color usage
- Predictable interaction patterns

### 2. Accessibility
- WCAG 2.1 AA compliant
- Keyboard navigation support
- Screen reader compatibility
- High contrast mode support

### 3. Responsiveness
- Mobile-first design approach
- Touch-friendly interaction targets
- Adaptive layouts for all screen sizes

### 4. Performance
- Lightweight and optimized
- Minimal DOM manipulation
- Efficient CSS selectors

## 🧩 Core Components

### Buttons

#### Primary Button
```html
<button class="btn btn-primary">
    <i class="fas fa-send"></i>
    Send Message
</button>
```

**Variants:**
- `btn-primary` - Main call-to-action
- `btn-secondary` - Secondary actions
- `btn-outline` - Outlined style
- `btn-ghost` - Minimal style

**Sizes:**
- `btn-sm` - Small (32px height)
- Default - Medium (44px height)
- `btn-lg` - Large (52px height)
- `btn-xl` - Extra large (60px height)

**States:**
- `:hover` - Elevated with shadow
- `:focus-visible` - Outline for keyboard navigation
- `:disabled` - Reduced opacity, no interactions

#### Usage Guidelines
- Use primary buttons for main actions (submit, send, save)
- Use secondary buttons for alternative actions (cancel, back)
- Ensure minimum 44px touch target for mobile
- Include loading states for async operations

### Form Components

#### Input Field
```html
<div class="form-group">
    <label class="form-label" for="email">Email Address</label>
    <input type="email" id="email" class="form-input" placeholder="Enter your email">
</div>
```

**Variants:**
- `form-input` - Standard text input
- `form-select` - Dropdown selection
- `form-textarea` - Multi-line text input

**States:**
- `is-valid` - Success state (green border)
- `is-invalid` - Error state (red border)
- `is-warning` - Warning state (yellow border)

#### Textarea
```html
<div class="form-group">
    <label class="form-label" for="message">Message</label>
    <textarea id="message" class="form-textarea" placeholder="Type your message..."></textarea>
</div>
```

**Features:**
- Auto-resize functionality
- Minimum height of 120px
- Vertical resize only

### Cards

#### Basic Card
```html
<div class="card">
    <div class="card-header">
        <h3>Card Title</h3>
    </div>
    <div class="card-body">
        <p>Card content goes here.</p>
    </div>
    <div class="card-footer">
        <button class="btn btn-primary">Action</button>
    </div>
</div>
```

**Features:**
- Hover elevation effect
- Rounded corners with shadow
- Optional header and footer sections
- Responsive padding

### Modals

#### Standard Modal
```html
<div class="modal" id="exampleModal" aria-hidden="true">
    <div class="modal-content">
        <div class="modal-header">
            <h2 class="modal-title">Modal Title</h2>
            <button class="modal-close" aria-label="Close modal">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <div class="modal-body">
            <p>Modal content goes here.</p>
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary" onclick="closeModal('exampleModal')">Cancel</button>
            <button class="btn btn-primary">Confirm</button>
        </div>
    </div>
</div>
```

**Features:**
- Backdrop blur effect
- Focus trapping
- Escape key to close
- Smooth animations
- Accessible markup

#### Mobile Modal
```html
<div class="mobile-modal" id="mobileModal">
    <div class="mobile-modal-header">
        <h2 class="mobile-modal-title">Mobile Modal</h2>
        <button class="mobile-modal-close">
            <i class="fas fa-times"></i>
        </button>
    </div>
    <div class="mobile-modal-body">
        <p>Mobile-optimized modal content.</p>
    </div>
</div>
```

**Features:**
- Full-screen on mobile
- Slide-up animation
- Touch-friendly close button
- Sticky header

### Alerts

#### Alert Messages
```html
<div class="alert alert-success">
    <i class="fas fa-check-circle"></i>
    <span>Success! Your message has been sent.</span>
</div>
```

**Variants:**
- `alert-success` - Green success message
- `alert-error` - Red error message
- `alert-warning` - Yellow warning message
- `alert-info` - Blue information message

**Features:**
- Icon support
- Dismissible option
- Auto-dismiss functionality
- Accessible announcements

### Loading Components

#### Spinner
```html
<div class="spinner"></div>
```

#### Skeleton Loader
```html
<div class="skeleton">
    <div class="skeleton-line"></div>
    <div class="skeleton-line"></div>
    <div class="skeleton-line short"></div>
</div>
```

**Features:**
- Animated shimmer effect
- Customizable dimensions
- Multiple line support
- Accessible loading states

### Progress Components

#### Progress Bar
```html
<div class="progress">
    <div class="progress-bar" style="--progress: 75%"></div>
</div>
```

**Features:**
- CSS custom property for progress value
- Smooth transitions
- Gradient background
- Accessible progress indication

### Badges

#### Status Badge
```html
<span class="badge badge-primary">New</span>
```

**Variants:**
- `badge-primary` - Primary color
- `badge-secondary` - Secondary color
- `badge-success` - Success color
- `badge-error` - Error color
- `badge-warning` - Warning color

## 📱 Mobile Components

### Mobile Navigation

#### Bottom Navigation
```html
<nav class="mobile-nav">
    <div class="mobile-nav-items">
        <a href="/" class="mobile-nav-item active">
            <i class="fas fa-home mobile-nav-icon"></i>
            <span class="mobile-nav-label">Home</span>
        </a>
        <a href="/chat" class="mobile-nav-item">
            <i class="fas fa-comments mobile-nav-icon"></i>
            <span class="mobile-nav-label">Chat</span>
        </a>
        <a href="/profile" class="mobile-nav-item">
            <i class="fas fa-user mobile-nav-icon"></i>
            <span class="mobile-nav-label">Profile</span>
        </a>
    </div>
</nav>
```

**Features:**
- Fixed bottom positioning
- Touch-optimized targets
- Active state indication
- Icon and label support

### Mobile Chat Interface

#### Chat Container
```html
<div class="mobile-chat-container">
    <div class="mobile-chat-header">
        <h2>AI Assistant</h2>
    </div>
    <div class="mobile-chat-messages" id="chatMessages">
        <!-- Messages go here -->
    </div>
    <div class="mobile-chat-input">
        <div class="mobile-chat-input-wrapper">
            <textarea class="mobile-chat-textarea" placeholder="Type a message..."></textarea>
            <button class="mobile-chat-send">
                <i class="fas fa-paper-plane"></i>
            </button>
        </div>
    </div>
</div>
```

#### Message Bubbles
```html
<!-- User Message -->
<div class="mobile-message mobile-message-user">
    <div class="mobile-message-bubble">
        Hello, how can you help me today?
    </div>
</div>

<!-- AI Message -->
<div class="mobile-message mobile-message-ai">
    <div class="mobile-message-bubble">
        I'm here to help you with your learning journey!
    </div>
</div>
```

**Features:**
- Distinct styling for user vs AI messages
- Responsive bubble sizing
- Smooth animations
- Touch-friendly interactions

### Mobile Tabs

#### Tab Navigation
```html
<div class="mobile-tabs">
    <button class="mobile-tab active">Overview</button>
    <button class="mobile-tab">Details</button>
    <button class="mobile-tab">Settings</button>
</div>
```

**Features:**
- Horizontal scrolling on overflow
- Active state indication
- Touch-friendly targets
- Smooth transitions

### Mobile Accordion

#### Collapsible Content
```html
<div class="mobile-accordion">
    <div class="mobile-accordion-item">
        <button class="mobile-accordion-header">
            <span>Section Title</span>
            <i class="fas fa-chevron-down mobile-accordion-icon"></i>
        </button>
        <div class="mobile-accordion-content">
            <div class="mobile-accordion-body">
                <p>Accordion content goes here.</p>
            </div>
        </div>
    </div>
</div>
```

**Features:**
- Smooth expand/collapse animations
- Rotating chevron icon
- Touch-optimized headers
- Accessible keyboard navigation

## 🎯 Interactive Components

### Floating Action Button

#### Mobile FAB
```html
<button class="mobile-fab">
    <i class="fas fa-plus"></i>
</button>
```

**Features:**
- Fixed positioning
- Elevation shadow
- Hover/touch effects
- Customizable icon

### Toast Notifications

#### Mobile Toast
```html
<div class="mobile-toast show">
    <span>Message sent successfully!</span>
</div>
```

**Features:**
- Bottom positioning on mobile
- Auto-dismiss functionality
- Slide-in animation
- Accessible announcements

### Swipe Actions

#### Swipeable Item
```html
<div class="mobile-swipe-item">
    <div class="mobile-swipe-content">
        <p>Swipe left to reveal actions</p>
    </div>
    <div class="mobile-swipe-actions">
        <i class="fas fa-trash"></i>
    </div>
</div>
```

**Features:**
- Touch gesture support
- Reveal actions on swipe
- Smooth animations
- Customizable actions

## 🎨 Utility Classes

### Layout Utilities

#### Flexbox
```html
<div class="flex items-center justify-between">
    <span>Left content</span>
    <span>Right content</span>
</div>
```

**Classes:**
- `flex` - Display flex
- `flex-col` - Flex direction column
- `items-center` - Align items center
- `justify-between` - Justify content space-between

#### Grid
```html
<div class="grid grid-cols-2 gap-md">
    <div>Item 1</div>
    <div>Item 2</div>
</div>
```

**Classes:**
- `grid` - Display grid
- `grid-cols-1` to `grid-cols-4` - Grid columns
- `gap-xs` to `gap-xl` - Grid gap

### Spacing Utilities

#### Padding and Margin
```html
<div class="p-lg m-md">Content with padding and margin</div>
```

**Classes:**
- `p-{size}` - Padding (xs, sm, md, lg, xl)
- `m-{size}` - Margin (xs, sm, md, lg, xl)

### Typography Utilities

#### Text Styling
```html
<h1 class="text-2xl font-bold text-center">Large Bold Centered Text</h1>
```

**Classes:**
- `text-xs` to `text-2xl` - Font sizes
- `font-light` to `font-bold` - Font weights
- `text-center`, `text-left`, `text-right` - Text alignment

### Color Utilities

#### Text Colors
```html
<p class="text-primary">Primary colored text</p>
```

**Classes:**
- `text-primary`, `text-secondary`, `text-tertiary` - Text colors
- `bg-primary`, `bg-secondary`, `bg-tertiary` - Background colors

## 🔧 JavaScript API

### Modal Management
```javascript
// Open modal
window.aadhyaUI.openModal('modalId');

// Close modal
window.aadhyaUI.closeModal(modalElement);
```

### Notifications
```javascript
// Show notification
window.aadhyaUI.showNotification('Success message', 'success', 5000);

// Show toast (mobile)
window.aadhyaUI.showToast('Quick message');
```

### Theme Management
```javascript
// Toggle theme
window.aadhyaUI.toggleTheme();

// Set specific theme
document.documentElement.setAttribute('data-theme', 'dark');
```

### Accessibility
```javascript
// Announce to screen readers
window.aadhyaUI.announce('Content updated');

// Focus management
window.aadhyaUI.trapFocus(containerElement);
```

## 📋 Usage Guidelines

### Do's
- ✅ Use consistent spacing from the design system
- ✅ Follow accessibility guidelines
- ✅ Test on mobile devices
- ✅ Use semantic HTML elements
- ✅ Provide loading states for async operations

### Don'ts
- ❌ Create custom colors outside the design system
- ❌ Use fixed pixel values for spacing
- ❌ Ignore keyboard navigation
- ❌ Create touch targets smaller than 44px
- ❌ Use color alone to convey information

## 🧪 Testing Components

### Accessibility Testing
```javascript
// Test keyboard navigation
// Tab through all interactive elements
// Ensure focus indicators are visible
// Test with screen reader

// Test color contrast
// Use browser dev tools or online tools
// Ensure minimum 4.5:1 ratio for normal text
// Ensure minimum 3:1 ratio for large text
```

### Mobile Testing
```javascript
// Test touch interactions
// Verify minimum 44px touch targets
// Test swipe gestures
// Test in both portrait and landscape
// Test on various screen sizes
```

### Performance Testing
```javascript
// Measure component render time
// Test with large datasets
// Monitor memory usage
// Test animation performance
```

## 🔄 Component Lifecycle

### Initialization
1. Component HTML is rendered
2. CSS classes are applied
3. JavaScript event listeners are attached
4. Accessibility attributes are set

### Interaction
1. User interaction triggers event
2. Component state is updated
3. Visual feedback is provided
4. Accessibility announcements are made

### Cleanup
1. Event listeners are removed
2. Timers are cleared
3. Memory is freed
4. DOM elements are cleaned up

## 📚 Examples and Demos

### Complete Form Example
```html
<form class="card">
    <div class="card-header">
        <h2>Contact Form</h2>
    </div>
    <div class="card-body">
        <div class="form-group">
            <label class="form-label" for="name">Name</label>
            <input type="text" id="name" class="form-input" required>
        </div>
        <div class="form-group">
            <label class="form-label" for="email">Email</label>
            <input type="email" id="email" class="form-input" required>
        </div>
        <div class="form-group">
            <label class="form-label" for="message">Message</label>
            <textarea id="message" class="form-textarea" required></textarea>
        </div>
    </div>
    <div class="card-footer">
        <button type="button" class="btn btn-secondary">Cancel</button>
        <button type="submit" class="btn btn-primary">
            <i class="fas fa-paper-plane"></i>
            Send Message
        </button>
    </div>
</form>
```

### Mobile Chat Interface Example
```html
<div class="mobile-chat-container">
    <div class="mobile-chat-header">
        <button class="mobile-menu-toggle">
            <i class="fas fa-bars"></i>
        </button>
        <h2>AI Assistant</h2>
        <button class="mobile-settings-btn">
            <i class="fas fa-cog"></i>
        </button>
    </div>
    
    <div class="mobile-chat-messages">
        <div class="mobile-message mobile-message-ai">
            <div class="mobile-message-bubble">
                Hello! How can I help you learn today?
            </div>
        </div>
        
        <div class="mobile-typing-indicator">
            <div class="mobile-typing-dots">
                <div class="mobile-typing-dot"></div>
                <div class="mobile-typing-dot"></div>
                <div class="mobile-typing-dot"></div>
            </div>
        </div>
    </div>
    
    <div class="mobile-chat-input">
        <div class="mobile-chat-input-wrapper">
            <button class="mobile-attachment-btn">
                <i class="fas fa-paperclip"></i>
            </button>
            <textarea class="mobile-chat-textarea" 
                     placeholder="Ask me anything..."></textarea>
            <button class="mobile-chat-send">
                <i class="fas fa-paper-plane"></i>
            </button>
        </div>
    </div>
</div>
```

This component library provides a comprehensive foundation for building consistent, accessible, and performant user interfaces across the Aadhya Eduverse platform.