/**
 * Aadhya Eduverse - Service Worker
 * Provides offline functionality and caching
 */

const CACHE_NAME = 'aadhya-eduverse-v1.0.0';
const OFFLINE_URL = '/offline.html';

// Resources to cache immediately
const PRECACHE_RESOURCES = [
    '/',
    '/login',
    '/register',
    '/css/style.css',
    '/css/mobile.css',
    '/js/app.js',
    '/manifest.json',
    OFFLINE_URL,
    // External resources
    'https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&family=Poppins:wght@300;400;500;600;700;800&display=swap',
    'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css'
];

// Resources to cache on first use
const RUNTIME_CACHE_RESOURCES = [
    '/api/auth/check',
    '/api/auth/login',
    '/api/auth/logout',
    '/api/auth/register'
];

// Install event - cache resources
self.addEventListener('install', event => {
    console.log('Service Worker installing...');
    
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => {
                console.log('Caching precache resources');
                return cache.addAll(PRECACHE_RESOURCES);
            })
            .then(() => {
                console.log('Service Worker installed successfully');
                return self.skipWaiting();
            })
            .catch(error => {
                console.error('Service Worker installation failed:', error);
            })
    );
});

// Activate event - clean up old caches
self.addEventListener('activate', event => {
    console.log('Service Worker activating...');
    
    event.waitUntil(
        caches.keys()
            .then(cacheNames => {
                return Promise.all(
                    cacheNames.map(cacheName => {
                        if (cacheName !== CACHE_NAME) {
                            console.log('Deleting old cache:', cacheName);
                            return caches.delete(cacheName);
                        }
                    })
                );
            })
            .then(() => {
                console.log('Service Worker activated successfully');
                return self.clients.claim();
            })
            .catch(error => {
                console.error('Service Worker activation failed:', error);
            })
    );
});

// Fetch event - serve cached content when offline
self.addEventListener('fetch', event => {
    const { request } = event;
    const url = new URL(request.url);
    
    // Skip non-GET requests
    if (request.method !== 'GET') {
        return;
    }
    
    // Skip chrome-extension and other non-http(s) requests
    if (!url.protocol.startsWith('http')) {
        return;
    }
    
    // Handle navigation requests
    if (request.mode === 'navigate') {
        event.respondWith(handleNavigationRequest(request));
        return;
    }
    
    // Handle API requests
    if (url.pathname.startsWith('/api/')) {
        event.respondWith(handleApiRequest(request));
        return;
    }
    
    // Handle static resources
    event.respondWith(handleStaticRequest(request));
});

// Handle navigation requests (HTML pages)
async function handleNavigationRequest(request) {
    try {
        // Try network first
        const networkResponse = await fetch(request);
        
        if (networkResponse.ok) {
            // Cache successful responses
            const cache = await caches.open(CACHE_NAME);
            cache.put(request, networkResponse.clone());
            return networkResponse;
        }
        
        throw new Error('Network response not ok');
    } catch (error) {
        console.log('Network failed, serving from cache:', error);
        
        // Try to serve from cache
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            return cachedResponse;
        }
        
        // Serve offline page as fallback
        const offlineResponse = await caches.match(OFFLINE_URL);
        if (offlineResponse) {
            return offlineResponse;
        }
        
        // Ultimate fallback
        return new Response(
            createOfflineHTML(),
            {
                status: 200,
                statusText: 'OK',
                headers: { 'Content-Type': 'text/html' }
            }
        );
    }
}

// Handle API requests
async function handleApiRequest(request) {
    try {
        // Always try network first for API requests
        const networkResponse = await fetch(request);
        
        if (networkResponse.ok) {
            // Cache successful GET responses
            if (request.method === 'GET') {
                const cache = await caches.open(CACHE_NAME);
                cache.put(request, networkResponse.clone());
            }
            return networkResponse;
        }
        
        throw new Error('API response not ok');
    } catch (error) {
        console.log('API request failed:', error);
        
        // For GET requests, try to serve from cache
        if (request.method === 'GET') {
            const cachedResponse = await caches.match(request);
            if (cachedResponse) {
                return cachedResponse;
            }
        }
        
        // Return error response for failed API requests
        return new Response(
            JSON.stringify({
                error: 'Network unavailable',
                message: 'Please check your internet connection and try again.',
                offline: true
            }),
            {
                status: 503,
                statusText: 'Service Unavailable',
                headers: { 'Content-Type': 'application/json' }
            }
        );
    }
}

// Handle static resource requests
async function handleStaticRequest(request) {
    try {
        // Try cache first for static resources
        const cachedResponse = await caches.match(request);
        if (cachedResponse) {
            return cachedResponse;
        }
        
        // Try network
        const networkResponse = await fetch(request);
        
        if (networkResponse.ok) {
            // Cache the response
            const cache = await caches.open(CACHE_NAME);
            cache.put(request, networkResponse.clone());
            return networkResponse;
        }
        
        throw new Error('Network response not ok');
    } catch (error) {
        console.log('Static resource request failed:', error);
        
        // Return a fallback response for failed static resources
        if (request.url.includes('.css')) {
            return new Response('/* Offline - CSS not available */', {
                headers: { 'Content-Type': 'text/css' }
            });
        }
        
        if (request.url.includes('.js')) {
            return new Response('// Offline - JavaScript not available', {
                headers: { 'Content-Type': 'application/javascript' }
            });
        }
        
        if (request.url.includes('.png') || request.url.includes('.jpg') || request.url.includes('.jpeg')) {
            // Return a simple 1x1 transparent pixel
            const pixel = 'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==';
            const buffer = Uint8Array.from(atob(pixel), c => c.charCodeAt(0));
            return new Response(buffer, {
                headers: { 'Content-Type': 'image/png' }
            });
        }
        
        // Generic fallback
        return new Response('Resource not available offline', {
            status: 404,
            statusText: 'Not Found'
        });
    }
}

// Create offline HTML page
function createOfflineHTML() {
    return `
<!DOCTYPE html>
<html lang="hi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Offline - Aadhya Eduverse</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            text-align: center;
            padding: 20px;
        }
        
        .offline-container {
            max-width: 400px;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 40px;
            border: 1px solid rgba(255, 255, 255, 0.2);
        }
        
        .offline-icon {
            font-size: 4rem;
            margin-bottom: 20px;
            opacity: 0.8;
        }
        
        .offline-title {
            font-size: 1.5rem;
            font-weight: 600;
            margin-bottom: 10px;
        }
        
        .offline-message {
            font-size: 1rem;
            opacity: 0.9;
            line-height: 1.6;
            margin-bottom: 30px;
        }
        
        .retry-button {
            background: rgba(255, 255, 255, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: white;
            padding: 12px 24px;
            border-radius: 10px;
            cursor: pointer;
            font-size: 1rem;
            transition: all 0.3s ease;
        }
        
        .retry-button:hover {
            background: rgba(255, 255, 255, 0.3);
            transform: translateY(-2px);
        }
        
        @media (max-width: 480px) {
            .offline-container {
                padding: 30px 20px;
            }
            
            .offline-icon {
                font-size: 3rem;
            }
            
            .offline-title {
                font-size: 1.25rem;
            }
        }
    </style>
</head>
<body>
    <div class="offline-container">
        <div class="offline-icon">📱</div>
        <h1 class="offline-title">You're Offline</h1>
        <p class="offline-message">
            It looks like you're not connected to the internet. 
            Please check your connection and try again.
        </p>
        <button class="retry-button" onclick="window.location.reload()">
            Try Again
        </button>
    </div>
</body>
</html>
    `;
}

// Handle background sync
self.addEventListener('sync', event => {
    console.log('Background sync triggered:', event.tag);
    
    if (event.tag === 'background-sync') {
        event.waitUntil(doBackgroundSync());
    }
});

// Background sync function
async function doBackgroundSync() {
    try {
        // Sync any pending data when connection is restored
        console.log('Performing background sync...');
        
        // You can add logic here to sync offline data
        // For example, send queued messages, sync user data, etc.
        
        // Notify all clients that sync is complete
        const clients = await self.clients.matchAll();
        clients.forEach(client => {
            client.postMessage({
                type: 'SYNC_COMPLETE',
                message: 'Background sync completed successfully'
            });
        });
    } catch (error) {
        console.error('Background sync failed:', error);
    }
}

// Handle push notifications (if needed in the future)
self.addEventListener('push', event => {
    console.log('Push notification received:', event);
    
    const options = {
        body: event.data ? event.data.text() : 'New message from Aadhya Eduverse',
        icon: '/icons/icon-192x192.png',
        badge: '/icons/badge-72x72.png',
        vibrate: [100, 50, 100],
        data: {
            dateOfArrival: Date.now(),
            primaryKey: 1
        },
        actions: [
            {
                action: 'explore',
                title: 'Open App',
                icon: '/icons/checkmark.png'
            },
            {
                action: 'close',
                title: 'Close',
                icon: '/icons/xmark.png'
            }
        ]
    };
    
    event.waitUntil(
        self.registration.showNotification('Aadhya Eduverse', options)
    );
});

// Handle notification clicks
self.addEventListener('notificationclick', event => {
    console.log('Notification clicked:', event);
    
    event.notification.close();
    
    if (event.action === 'explore') {
        event.waitUntil(
            clients.openWindow('/')
        );
    }
});

// Handle messages from the main thread
self.addEventListener('message', event => {
    console.log('Service Worker received message:', event.data);
    
    if (event.data && event.data.type === 'SKIP_WAITING') {
        self.skipWaiting();
    }
    
    if (event.data && event.data.type === 'GET_VERSION') {
        event.ports[0].postMessage({ version: CACHE_NAME });
    }
});

// Periodic background sync (if supported)
self.addEventListener('periodicsync', event => {
    console.log('Periodic sync triggered:', event.tag);
    
    if (event.tag === 'content-sync') {
        event.waitUntil(doPeriodicSync());
    }
});

// Periodic sync function
async function doPeriodicSync() {
    try {
        console.log('Performing periodic sync...');
        
        // Add logic for periodic content updates
        // For example, prefetch new educational content, update cache, etc.
        
    } catch (error) {
        console.error('Periodic sync failed:', error);
    }
}

console.log('Service Worker loaded successfully');