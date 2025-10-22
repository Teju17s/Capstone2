// src/utils/logger.js

const LOG_LEVELS = {
    ERROR: 0,
    WARN: 1,
    INFO: 2,
    DEBUG: 3
};

class Logger {
    constructor() {
        this.level = this.getLogLevel();
        this.isDevelopment = import.meta.env.DEV;
    }

    getLogLevel() {
        const envLevel = import.meta.env.VITE_LOG_LEVEL || 'INFO';
        return LOG_LEVELS[envLevel.toUpperCase()] ?? LOG_LEVELS.INFO;
    }

    formatMessage(level, message, context = {}) {
        const timestamp = new Date().toISOString();
        const logEntry = { timestamp, level, message, ...context };
        return this.isDevelopment
            ? `[${timestamp}] ${level}: ${message}`
            : JSON.stringify(logEntry);
    }

    shouldLog(level) {
        return LOG_LEVELS[level] <= this.level;
    }

    error(message, error = null, context = {}) {
        if (!this.shouldLog('ERROR')) return;

        const errorContext = {
            ...context,
            ...(error && {
                errorName: error.name,
                errorMessage: error.message,
                errorStack: error.stack,
                ...(error.response && {
                    httpStatus: error.response.status,
                    httpData: error.response.data
                })
            })
        };

        const formattedMessage = this.formatMessage('ERROR', message, errorContext);
        this.isDevelopment ? console.error(formattedMessage, error) : console.error(formattedMessage);
    }

    warn(message, context = {}) {
        if (!this.shouldLog('WARN')) return;
        console.warn(this.formatMessage('WARN', message, context));
    }

    info(message, context = {}) {
        if (!this.shouldLog('INFO')) return;
        console.info(this.formatMessage('INFO', message, context));
    }

    debug(message, context = {}) {
        if (!this.shouldLog('DEBUG')) return;
        console.debug(this.formatMessage('DEBUG', message, context));
    }

    // ---------- Helper Methods ----------

    logApiError(endpoint, error, requestId = null) {
        this.error(`API request failed: ${endpoint}`, error, {
            endpoint,
            requestId,
            userAgent: navigator.userAgent,
            url: window.location.href
        });
    }

    logUserAction(action, details = {}) {
        this.info(`User action: ${action}`, {
            action,
            ...details,
            userId: this.getCurrentUserId(),
            sessionId: this.getSessionId()
        });
    }

    getCurrentUserId() {
        return localStorage.getItem('userId') || 'anonymous';
    }

    getSessionId() {
        let sessionId = sessionStorage.getItem('sessionId');
        if (!sessionId) {
            sessionId = `session_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
            sessionStorage.setItem('sessionId', sessionId);
        }
        return sessionId;
    }
}

// Singleton instance
const logger = new Logger();
export default logger;
