package me.spartacus04.jext.webapi.auth

import me.spartacus04.jext.utils.constantTimeEquals
import java.util.concurrent.ConcurrentHashMap

/**
 * The object `SessionRegistry` keeps track of the authenticated web interface sessions.
 *
 * Sessions are keyed by the client's IP address, hold the bearer token, and expire after
 * [SESSION_TTL_MS] milliseconds. All access is thread-safe.
 */
internal object SessionRegistry {
    private const val SESSION_TTL_MS = 12 * 60 * 60 * 1000L // 12 hours

    internal data class Session(val token: String, val createdAt: Long)

    private val sessions = ConcurrentHashMap<String, Session>()

    /**
     * Creates a new session for the given address, replacing any previous one.
     *
     * @param addr The address of the client.
     * @param token The bearer token of the session.
     */
    fun create(addr: String, token: String) {
        sessions[addr] = Session(token, System.currentTimeMillis())
    }

    /**
     * Checks whether the given address/token pair matches an existing, non-expired session.
     *
     * @param addr The address of the client.
     * @param token The bearer token provided by the client.
     *
     * @return Whether the pair is valid.
     */
    fun isValid(addr: String, token: String): Boolean {
        val session = sessions[addr] ?: return false

        if(System.currentTimeMillis() - session.createdAt > SESSION_TTL_MS) {
            sessions.remove(addr, session)
            return false
        }

        return constantTimeEquals(session.token, token)
    }

    /**
     * Removes the session of the given address, if present.
     *
     * @param addr The address of the client.
     */
    fun remove(addr: String) {
        sessions.remove(addr)
    }
}
