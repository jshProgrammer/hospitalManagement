package org.hospitalmanagement.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

@Service
class CryptoUtility(
    @Value("\${encryption.key:}")
    private val encryptionKeyHex: String,
    @Value("\${encryption.salt:}")
    private val salt: String
) {
    companion object {
        private const val ALGORITHM = "AES"
        private const val CIPHER_MODE = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val GCM_IV_LENGTH_BYTES = 12
        private const val HMAC_ALGORITHM = "HmacSHA256"
        private const val HASH_ALGORITHM = "SHA-256"
    }

    private val masterKey: ByteArray by lazy {
        if (encryptionKeyHex.length != 64) {
            throw IllegalStateException("ENCRYPTION_KEY must be 64 hex characters (256-bit), got ${encryptionKeyHex.length}")
        }
        hexToByteArray(encryptionKeyHex)
    }

    private val hashKey: ByteArray by lazy {
        deriveKeyUsingHKDF(masterKey, salt.toByteArray(), "BLIND_INDEX_KEY")
    }

    /**
     * Encrypts plaintext using AES-256-GCM with random IV
     * Returns: Base64-encoded string of IV + ciphertext + authTag
     */
    fun encrypt(plaintext: String): String {
        if (plaintext.isBlank()) return ""

        val cipher = Cipher.getInstance(CIPHER_MODE)
        val random = SecureRandom()
        val iv = ByteArray(GCM_IV_LENGTH_BYTES)
        random.nextBytes(iv)

        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        val keySpec = SecretKeySpec(masterKey, 0, masterKey.size, ALGORITHM)

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
        val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

        // Combine IV + ciphertext for storage
        val combined = iv + ciphertext
        return Base64.getEncoder().encodeToString(combined)
    }

    /**
     * Decrypts ciphertext encrypted with AES-256-GCM
     * Expects: Base64-encoded string of IV + ciphertext + authTag
     */
    fun decrypt(encryptedBase64: String): String {
        if (encryptedBase64.isBlank()) return ""

        val combined = Base64.getDecoder().decode(encryptedBase64)
        val iv = combined.sliceArray(0 until GCM_IV_LENGTH_BYTES)
        val ciphertext = combined.sliceArray(GCM_IV_LENGTH_BYTES until combined.size)

        val cipher = Cipher.getInstance(CIPHER_MODE)
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        val keySpec = SecretKeySpec(masterKey, 0, masterKey.size, ALGORITHM)

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
        val plaintext = cipher.doFinal(ciphertext)

        return String(plaintext, Charsets.UTF_8)
    }

    /**
     * Generates deterministic HMAC-SHA256 hash for blind indexing
     * Same plaintext always produces same hash (deterministic)
     */
    fun generateBlindIndex(plaintext: String): String {
        if (plaintext.isBlank()) return ""

        val mac = Mac.getInstance(HMAC_ALGORITHM)
        val keySpec = SecretKeySpec(hashKey, 0, hashKey.size, HMAC_ALGORITHM)
        mac.init(keySpec)

        val hash = mac.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(hash)
    }

    /**
     * Verify plaintext matches blind index
     */
    fun verifyBlindIndex(plaintext: String, blindIndex: String): Boolean {
        return generateBlindIndex(plaintext) == blindIndex
    }

    /**
     * HKDF-SHA256 based key derivation (simplified version)
     * Derives a 32-byte (256-bit) key from master key + salt + info
     */
    private fun deriveKeyUsingHKDF(
        ikm: ByteArray,
        salt: ByteArray,
        info: String
    ): ByteArray {
        // Simple HKDF-SHA256 implementation (extract-expand)
        val hmac = Mac.getInstance(HMAC_ALGORITHM)

        // Extract phase
        val prk = if (salt.isEmpty()) {
            val zeroSalt = ByteArray(32)
            hmac.init(SecretKeySpec(zeroSalt, 0, zeroSalt.size, HMAC_ALGORITHM))
            hmac.doFinal(ikm)
        } else {
            hmac.init(SecretKeySpec(salt, 0, salt.size, HMAC_ALGORITHM))
            hmac.doFinal(ikm)
        }

        // Expand phase
        hmac.init(SecretKeySpec(prk, 0, prk.size, HMAC_ALGORITHM))
        val infoBytes = info.toByteArray(Charsets.UTF_8)
        val result = ByteArray(32)
        val hash1 = hmac.doFinal(infoBytes + 0x01.toByte())
        System.arraycopy(hash1, 0, result, 0, minOf(32, hash1.size))

        return result
    }

    /**
     * Convert hex string to byte array
     */
    private fun hexToByteArray(hexString: String): ByteArray {
        val result = ByteArray(hexString.length / 2)
        for (i in result.indices) {
            val index = i * 2
            val highByte = hexString.substring(index, index + 1).toInt(16)
            val lowByte = hexString.substring(index + 1, index + 2).toInt(16)
            result[i] = ((highByte shl 4) or lowByte).toByte()
        }
        return result
    }
}
