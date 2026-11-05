package me.spartacus04.jext.webapi.auth

import java.util.concurrent.ConcurrentHashMap

/**
 * The object `ConnectRateLimiter` provides brute-force protection for the web interface login.
 *
 * An address is blocked for a cool-down window after a number of consecutive failed login
 * attempts, and unblocks `WINDOW_MS` milliseconds after the last failed attempt. All access
 * is thread-safe.
 */
internal object ConnectRateLimiter {
    private const val MAX_ATTEMPTS = 5
    private const val RETRY_AFTER_SECONDS = 600L

    private const val WINDOW_MS = RETRY_AFTER_SECONDS * 1000L // 10 minutes

    private const val MAX_TRACKED_ADDRESSES = 10_000

    private data class Failure(val count: Int, val lastAttemptMs: Long)

    private val failures = ConcurrentHashMap<String, Failure>()

    /**
     * Checks whether the given address is currently blocked from attempting to log in.
     *
     * @param addr The address of the client.
     *
     * @return Whether the address is blocked.
     */
    fun isBlocked(addr: String): Boolean {
        val failure = failures[addr] ?: return false

        if(System.currentTimeMillis() - failure.lastAttemptMs > WINDOW_MS) {
            failures.remove(addr, failure)
            return false
        }

        return failure.count >= MAX_ATTEMPTS
    }

    /**
     * Records a failed login attempt for the given address.
     *
     * @param addr The address of the client.
     */
    fun recordFailure(addr: String) {
        val now = System.currentTimeMillis()

        failures.compute(addr) { _, failure ->
            val count = if(failure == null || now - failure.lastAttemptMs > WINDOW_MS) 1 else {
                failure.count + 1
            }

            Failure(count, now)
        }

        // avoid unbounded growth if an attacker distributes failures over many addresses
        if(failures.size > MAX_TRACKED_ADDRESSES) {
            failures.entries.removeIf { now - it.value.lastAttemptMs > WINDOW_MS }
        }
    }

    /**
     * Clears the recorded failures for the given address, e.g. after a successful login.
     *
     * @param addr The address of the client.
     */
    fun clear(addr: String) {
        failures.remove(addr)
    }

    /**
     * The cool-down, in seconds, clients should wait before retrying.
     */
    internal const val RETRY_AFTER = RETRY_AFTER_SECONDS
}
