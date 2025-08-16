/**
 * Aadhya Eduverse - Modern UI Interactions
 * Enhanced JavaScript for world-class user experience
 */

class AadhyaUI {
    constructor() {
        this.init();
    }

    init() {
        this.setupGlobalEventListeners();
        this.setupAnimations();
        this.setupAccessibility();
        this.setupPerformanceOptimizations();
    }

    // Global Event Listeners
    setupGlobalEventListeners() {
        // Smooth scrolling for anchor links
        document.addEventListener('click', (e) => {
            if (e.target.matches('a[href^="#"]')) {
                e.preventDefault();
                const target = document.querySelector(e.target.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            }
        });

        // Enhanced ripple effect for buttons
        this.setupRippleEffect();

        // Keyboard navigation improvements
        this.setupKeyboardNavigation();

        // Touch gestures for mobile
        this.setupTouchGestures();
    }

    // Ripple Effect
    setupRippleEffect() {
        document.addEventListener('click', (e) => {
            if (e.target.matches('button, .btn, .ripple, .today-history-btn')) {
                this.createRipple(e);
            }
        });
    }

    createRipple(event) {
        const button = event.target;
        
        // Safety check to ensure it's a DOM element
        if (!button || typeof button.getBoundingClientRect !== 'function') {
            return;
        }
        
        const ripple = document.createElement('span');
        const rect = button.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = event.clientX - rect.left - size / 2;
        const y = event.clientY - rect.top - size / 2;

        ripple.style.cssText = `
            position: absolute;
            width: ${size}px;
            height: ${size}px;
            left: ${x}px;
            top: ${y}px;
            background: rgba(255, 255, 255, 0.3);
            border-radius: 50%;
            transform: scale(0);
            animation: ripple 0.6s linear;
            pointer-events: none;
            z-index: 1000;
        `;

        // Ensure button has relative positioning
        const originalPosition = getComputedStyle(button).position;
        if (originalPosition === 'static') {
            button.style.position = 'relative';
        }
        button.style.overflow = 'hidden';

        button.appendChild(ripple);

        setTimeout(() => {
            ripple.remove();
        }, 600);
    }

    // Animations
    setupAnimations() {
        // Intersection Observer for scroll animations
        this.setupScrollAnimations();

        // Parallax effects
        this.setupParallaxEffects();

        // Loading animations
        this.setupLoadingAnimations();
    }

    setupScrollAnimations() {
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-in');
                } else {
                    entry.target.classList.remove('animate-in');
                }
            });
        }, observerOptions);

        // Observe elements with animation classes
        document.querySelectorAll('.animate-on-scroll').forEach(el => {
            observer.observe(el);
        });
    }

    setupParallaxEffects() {
        let ticking = false;

        const updateParallax = () => {
            const scrolled = window.pageYOffset;
            const parallaxElements = document.querySelectorAll('.parallax');

            parallaxElements.forEach(element => {
                const speed = element.dataset.speed || 0.5;
                const yPos = -(scrolled * speed);
                element.style.transform = `translateY(${yPos}px)`;
            });

            ticking = false;
        };

        window.addEventListener('scroll', () => {
            if (!ticking) {
                requestAnimationFrame(updateParallax);
                ticking = true;
            }
        });
    }

    setupLoadingAnimations() {
        // Skeleton loading for content
        this.createSkeletonLoaders();

        // Progressive image loading
        this.setupProgressiveImageLoading();
    }

    createSkeletonLoaders() {
        const skeletonElements = document.querySelectorAll('.skeleton');
        
        skeletonElements.forEach(element => {
            element.innerHTML = `
                <div class="skeleton-content">
                    <div class="skeleton-line"></div>
                    <div class="skeleton-line"></div>
                    <div class="skeleton-line short"></div>
                </div>
            `;
        });
    }

    setupProgressiveImageLoading() {
        const images = document.querySelectorAll('img[data-src]');
        
        const imageObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        images.forEach(img => imageObserver.observe(img));
    }

    // Accessibility
    setupAccessibility() {
        // Focus management
        this.setupFocusManagement();

        // ARIA live regions
        this.setupAriaLiveRegions();

        // High contrast mode detection
        this.setupHighContrastMode();
    }

    setupFocusManagement() {
        // Trap focus in modals
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Tab') {
                const modal = document.querySelector('.modal.active');
                if (modal) {
                    this.trapFocus(e, modal);
                }
            }
        });

        // Skip to main content
        this.createSkipLink();
    }

    trapFocus(event, container) {
        const focusableElements = container.querySelectorAll(
            'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
        );
        
        const firstElement = focusableElements[0];
        const lastElement = focusableElements[focusableElements.length - 1];

        if (event.shiftKey && document.activeElement === firstElement) {
            event.preventDefault();
            lastElement.focus();
        } else if (!event.shiftKey && document.activeElement === lastElement) {
            event.preventDefault();
            firstElement.focus();
        }
    }

    createSkipLink() {
        const skipLink = document.createElement('a');
        skipLink.href = '#main-content';
        skipLink.textContent = 'Skip to main content';
        skipLink.className = 'skip-link';
        skipLink.style.cssText = `
            position: absolute;
            top: -40px;
            left: 6px;
            background: var(--primary-color);
            color: white;
            padding: 8px;
            text-decoration: none;
            border-radius: 4px;
            z-index: 10000;
            transition: top 0.3s;
        `;

        skipLink.addEventListener('focus', () => {
            skipLink.style.top = '6px';
        });

        skipLink.addEventListener('blur', () => {
            skipLink.style.top = '-40px';
        });

        document.body.insertBefore(skipLink, document.body.firstChild);
    }

    setupAriaLiveRegions() {
        // Create live region for announcements
        const liveRegion = document.createElement('div');
        liveRegion.setAttribute('aria-live', 'polite');
        liveRegion.setAttribute('aria-atomic', 'true');
        liveRegion.className = 'sr-only';
        liveRegion.id = 'live-region';
        document.body.appendChild(liveRegion);
    }

    setupHighContrastMode() {
        // Detect high contrast mode
        if (window.matchMedia('(prefers-contrast: high)').matches) {
            document.body.classList.add('high-contrast');
        }
    }

    // Keyboard Navigation
    setupKeyboardNavigation() {
        document.addEventListener('keydown', (e) => {
            // Escape key handling
            if (e.key === 'Escape') {
                this.handleEscapeKey();
            }

            // Arrow key navigation for custom components
            if (['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(e.key)) {
                this.handleArrowKeys(e);
            }
        });
    }

    handleEscapeKey() {
        // Close modals
        const activeModal = document.querySelector('.modal.active');
        if (activeModal) {
            this.closeModal(activeModal);
        }

        // Close dropdowns
        const activeDropdown = document.querySelector('.dropdown.active');
        if (activeDropdown) {
            activeDropdown.classList.remove('active');
        }
    }

    handleArrowKeys(event) {
        const target = event.target;
        
        // Handle custom select components
        if (target.matches('.custom-select')) {
            event.preventDefault();
            // Custom select navigation logic
        }

        // Handle tab navigation
        if (target.matches('.tab-button')) {
            event.preventDefault();
            this.navigateTabs(event);
        }
    }

    navigateTabs(event) {
        const currentTab = event.target;
        const tabList = currentTab.closest('.tab-list');
        const tabs = Array.from(tabList.querySelectorAll('.tab-button'));
        const currentIndex = tabs.indexOf(currentTab);

        let nextIndex;
        if (event.key === 'ArrowLeft') {
            nextIndex = currentIndex > 0 ? currentIndex - 1 : tabs.length - 1;
        } else if (event.key === 'ArrowRight') {
            nextIndex = currentIndex < tabs.length - 1 ? currentIndex + 1 : 0;
        }

        if (nextIndex !== undefined) {
            tabs[nextIndex].focus();
            tabs[nextIndex].click();
        }
    }

    // Touch Gestures
    setupTouchGestures() {
        let startX, startY, startTime;

        document.addEventListener('touchstart', (e) => {
            const touch = e.touches[0];
            startX = touch.clientX;
            startY = touch.clientY;
            startTime = Date.now();
        }, { passive: true });

        document.addEventListener('touchend', (e) => {
            if (!startX || !startY) return;

            const touch = e.changedTouches[0];
            const endX = touch.clientX;
            const endY = touch.clientY;
            const endTime = Date.now();

            const deltaX = endX - startX;
            const deltaY = endY - startY;
            const deltaTime = endTime - startTime;

            // Swipe detection
            if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 50 && deltaTime < 300) {
                if (deltaX > 0) {
                    this.handleSwipeRight(e.target);
                } else {
                    this.handleSwipeLeft(e.target);
                }
            }

            // Reset
            startX = startY = null;
        }, { passive: true });
    }

    handleSwipeLeft(target) {
        // Handle swipe left gestures
        const carousel = target.closest('.carousel');
        if (carousel) {
            this.nextSlide(carousel);
        }
    }

    handleSwipeRight(target) {
        // Handle swipe right gestures
        const carousel = target.closest('.carousel');
        if (carousel) {
            this.prevSlide(carousel);
        }
    }

    // Performance Optimizations
    setupPerformanceOptimizations() {
        // Debounce scroll events
        this.debounceScrollEvents();

        // Lazy load components
        this.setupLazyLoading();

        // Preload critical resources
        this.preloadCriticalResources();
    }

    debounceScrollEvents() {
        let scrollTimeout;
        
        window.addEventListener('scroll', () => {
            if (scrollTimeout) {
                clearTimeout(scrollTimeout);
            }
            
            scrollTimeout = setTimeout(() => {
                // Scroll event handling
                this.handleScroll();
            }, 16); // ~60fps
        }, { passive: true });
    }

    handleScroll() {
        // Update scroll-based animations
        const scrolled = window.pageYOffset;
        const windowHeight = window.innerHeight;
        
        // Update progress indicators
        const progressBars = document.querySelectorAll('.progress-bar');
        progressBars.forEach(bar => {
            const rect = bar.getBoundingClientRect();
            if (rect.top < windowHeight && rect.bottom > 0) {
                const progress = Math.min(100, Math.max(0, 
                    ((windowHeight - rect.top) / windowHeight) * 100
                ));
                bar.style.setProperty('--progress', `${progress}%`);
            }
        });
    }

    setupLazyLoading() {
        // Lazy load heavy components
        const lazyComponents = document.querySelectorAll('[data-lazy-component]');
        
        const componentObserver = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const component = entry.target;
                    const componentName = component.dataset.lazyComponent;
                    this.loadComponent(componentName, component);
                    componentObserver.unobserve(component);
                }
            });
        });

        lazyComponents.forEach(component => {
            componentObserver.observe(component);
        });
    }

    loadComponent(name, container) {
        // Dynamic component loading
        switch (name) {
            case 'chart':
                this.loadChartComponent(container);
                break;
            case 'map':
                this.loadMapComponent(container);
                break;
            default:
                console.warn(`Unknown component: ${name}`);
        }
    }

    preloadCriticalResources() {
        // Preload critical fonts
        const criticalFonts = [
            'https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap'
        ];

        criticalFonts.forEach(font => {
            const link = document.createElement('link');
            link.rel = 'preload';
            link.as = 'style';
            link.href = font;
            document.head.appendChild(link);
        });
    }

    // Utility Methods
    announce(message) {
        const liveRegion = document.getElementById('live-region');
        if (liveRegion) {
            liveRegion.textContent = message;
            setTimeout(() => {
                liveRegion.textContent = '';
            }, 1000);
        }
    }

    showNotification(message, type = 'info', duration = 5000) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <div class="notification-content">
                <i class="fas fa-${this.getNotificationIcon(type)}"></i>
                <span>${message}</span>
                <button class="notification-close" onclick="this.parentElement.parentElement.remove()">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        `;

        // Add to notification container or create one
        let container = document.querySelector('.notification-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'notification-container';
            document.body.appendChild(container);
        }

        container.appendChild(notification);

        // Auto remove
        setTimeout(() => {
            if (notification.parentElement) {
                notification.remove();
            }
        }, duration);

        // Announce to screen readers
        this.announce(message);
    }

    getNotificationIcon(type) {
        const icons = {
            success: 'check-circle',
            error: 'exclamation-triangle',
            warning: 'exclamation-circle',
            info: 'info-circle'
        };
        return icons[type] || icons.info;
    }

    // Modal Management
    openModal(modalId) {
        const modal = document.getElementById(modalId);
        if (modal) {
            modal.classList.add('active');
            modal.setAttribute('aria-hidden', 'false');
            
            // Focus first focusable element
            const firstFocusable = modal.querySelector(
                'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
            );
            if (firstFocusable) {
                firstFocusable.focus();
            }

            // Prevent body scroll
            document.body.style.overflow = 'hidden';
        }
    }

    closeModal(modal) {
        modal.classList.remove('active');
        modal.setAttribute('aria-hidden', 'true');
        
        // Restore body scroll
        document.body.style.overflow = '';
        
        // Return focus to trigger element
        const trigger = document.querySelector(`[data-modal="${modal.id}"]`);
        if (trigger) {
            trigger.focus();
        }
    }

    // Theme Management
    toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme');
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme);
        
        this.announce(`Switched to ${newTheme} theme`);
    }

    initTheme() {
        const savedTheme = localStorage.getItem('theme');
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
        const theme = savedTheme || (prefersDark ? 'dark' : 'light');
        
        document.documentElement.setAttribute('data-theme', theme);
    }
}

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    window.aadhyaUI = new AadhyaUI();
});

// Add CSS for animations and components
const dynamicStyles = `
    @keyframes ripple {
        to {
            transform: scale(4);
            opacity: 0;
        }
    }

    .animate-on-scroll {
        opacity: 0;
        transform: translateY(30px);
        transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
    }

    .animate-on-scroll.animate-in {
        opacity: 1;
        transform: translateY(0);
    }

    .skeleton {
        background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
        background-size: 200% 100%;
        animation: loading 1.5s infinite;
    }

    .skeleton-line {
        height: 1rem;
        background: inherit;
        border-radius: 4px;
        margin-bottom: 0.5rem;
    }

    .skeleton-line.short {
        width: 60%;
    }

    @keyframes loading {
        0% { background-position: 200% 0; }
        100% { background-position: -200% 0; }
    }

    .notification-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 10000;
        max-width: 400px;
    }

    .notification {
        background: white;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        margin-bottom: 10px;
        animation: slideInRight 0.3s ease-out;
    }

    .notification-content {
        padding: 16px;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .notification-close {
        background: none;
        border: none;
        cursor: pointer;
        margin-left: auto;
        opacity: 0.7;
        transition: opacity 0.2s;
    }

    .notification-close:hover {
        opacity: 1;
    }

    @keyframes slideInRight {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }

    .sr-only {
        position: absolute;
        width: 1px;
        height: 1px;
        padding: 0;
        margin: -1px;
        overflow: hidden;
        clip: rect(0, 0, 0, 0);
        white-space: nowrap;
        border: 0;
    }

    .high-contrast {
        filter: contrast(150%);
    }

    @media (prefers-reduced-motion: reduce) {
        *, *::before, *::after {
            animation-duration: 0.01ms !important;
            animation-iteration-count: 1 !important;
            transition-duration: 0.01ms !important;
        }
    }
`;

// Inject dynamic styles
const styleSheet = document.createElement('style');
styleSheet.textContent = dynamicStyles;
document.head.appendChild(styleSheet);

// Export for use in other modules
if (typeof module !== 'undefined' && module.exports) {
    module.exports = AadhyaUI;
}