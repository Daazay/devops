package org.daazay.domain.model

data class SaltedHash(
    val hash: String,
    val salt: String,
)