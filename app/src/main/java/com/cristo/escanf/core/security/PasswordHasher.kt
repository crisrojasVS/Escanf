package com.cristo.escanf.core.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordHasher @Inject constructor() {

    private val iterations = 10_000
    private val keyLength = 256
    private val random = SecureRandom()

    /** Devuelve "salt:hash" en Base64. */
    fun hash(password: String): String {
        val salt = ByteArray(16).also { random.nextBytes(it) }
        val hash = derive(password, salt)
        val encoder = Base64.getEncoder()
        return "${encoder.encodeToString(salt)}:${encoder.encodeToString(hash)}"
    }

    fun verify(password: String, stored: String): Boolean {
        val parts = stored.split(":")
        if (parts.size != 2) return false
        val decoder = Base64.getDecoder()
        val salt = decoder.decode(parts[0])
        val expected = decoder.decode(parts[1])
        return MessageDigest.isEqual(expected, derive(password, salt))
    }

    private fun derive(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, keyLength)
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            .generateSecret(spec)
            .encoded
    }
}