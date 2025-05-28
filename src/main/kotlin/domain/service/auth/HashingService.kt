package org.daazay.domain.service.auth

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.daazay.domain.model.auth.SaltedHash
import kotlin.random.Random

class HashingService {
    fun generateSaltedHash(value: String): SaltedHash {
        val salt = Random.nextBytes(32)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(hash, saltAsHex)
    }

    fun verify(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + value) == saltedHash.hash
    }
}