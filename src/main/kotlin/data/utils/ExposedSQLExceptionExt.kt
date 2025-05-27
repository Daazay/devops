package org.daazay.data.utils

import org.jetbrains.exposed.exceptions.ExposedSQLException

fun ExposedSQLException.isUniqueConstraintViolation(): Boolean {
    val sqlException = cause as java.sql.SQLException? ?: return false
    return sqlException.isUniqueConstraintViolation()
}

fun java.sql.SQLException.isUniqueConstraintViolation(): Boolean {
    return when(errorCode) {
        23505 -> true   // PostgreSQL, H2
        1062 -> true    // MySQL
        else -> sqlState.startsWith("23")
    }
}