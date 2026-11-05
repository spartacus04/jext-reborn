package me.spartacus04.jext.utils

import java.security.SecureRandom
import java.security.MessageDigest

private val secureRandom = SecureRandom()

private const val ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

/**
 * Generates a cryptographically secure random token made of alphanumeric characters.
 *
 * @param length The length of the token, defaults to 64.
 *
 * @return The generated token.
 */
internal fun generateSecureToken(length: Int = 64): String {
    return (1..length).map { ALPHANUMERIC[secureRandom.nextInt(ALPHANUMERIC.length)] }.joinToString("")
}

/**
 * Generates a cryptographically secure random password made of alphanumeric characters (32 characters long).
 *
 * @return The generated password.
 */
internal fun generateSecurePassword(length: Int = 32): String {
    return generateSecureToken(length)
}

/**
 * Compares two strings in constant time. Both strings are hashed to a fixed length before comparing,
 * so no length information leaks either.
 *
 * @param a The first string.
 * @param b The second string.
 *
 * @return Whether the two strings are equal.
 */
internal fun constantTimeEquals(a: String, b: String): Boolean {
    val digest = MessageDigest.getInstance("SHA-256")

    val hashA = digest.digest(a.toByteArray(Charsets.UTF_8))
    val hashB = digest.digest(b.toByteArray(Charsets.UTF_8))

    return MessageDigest.isEqual(hashA, hashB)
}

private val safePathSegmentRegex = Regex("[A-Za-z0-9._-]+")

/**
 * Validates a user-configurable value used as a path segment, preventing path traversal.
 *
 * @param value The value to validate.
 * @param fallback The value to return if the original is not a safe path segment.
 *
 * @return The validated value, or the fallback.
 */
internal fun sanitizePathSegment(value: String, fallback: String): String {
    return if (value.isNotBlank()
        && !value.startsWith(".")
        && safePathSegmentRegex.matches(value)
    ) value else fallback
}
