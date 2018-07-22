package com.ongoni.onlineshop.utils

import java.security.MessageDigest
import java.util.*

fun String.hashed(type: String = "SHA-512"): String = hash(type, this.toByteArray())

fun generateToken(): String = hash(type = "SHA-256", bytes = getRandomBytes())

private fun getRandomBytes(size: Int = 32): ByteArray {
    val bytes = ByteArray(size)
    Random().nextBytes(bytes)

    return bytes
}

private fun bytesToString(bytes: ByteArray): String {
    val hexChars = "0123456789abcdef"
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(hexChars[i shr 4 and 0x0f])
        result.append(hexChars[i and 0x0f])
    }

    return result.toString()
}

private fun hash(type: String, bytes: ByteArray): String = bytesToString(MessageDigest
            .getInstance(type)
            .digest(bytes))
