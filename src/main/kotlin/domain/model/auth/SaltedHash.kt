package org.daazay.domain.model.auth

data class SaltedHash(
    val hash: String,
    val salt: String,
)