package org.daazay.utils

inline fun <reified T> requireEnv(
    name: String,
    default: (() -> T) = { throw IllegalArgumentException("Missing required ENV '$name.'") }
): T {
    return System.getenv(name)?.let { value ->
        when(T::class) {
            Int::class -> value.toInt() as? T
            Long::class -> value.toLong() as? T
            Float::class -> value.toFloat() as? T
            Double::class -> value.toDouble() as? T
            Boolean::class -> value.toBoolean() as T
            else -> value as T
        }
    } ?: default()
}