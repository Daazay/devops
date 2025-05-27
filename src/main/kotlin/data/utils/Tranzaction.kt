package org.daazay.data.utils

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction

fun<T> tranzaction(
    onUniqueConstraintViolation: (() -> Unit) = { },
    inner: () -> T,
): T = transaction {
    try {
        return@transaction inner()
    } catch (e: ExposedSQLException) {
        rollback()
        if (e.isUniqueConstraintViolation()) {
            onUniqueConstraintViolation()
        }
        throw e
    }
}