package com.mcstarrysky.util

import java.security.MessageDigest

fun String.hashPassword(): String {
    val md5 = MessageDigest.getInstance("MD5")
    val digest = md5.digest(this.toByteArray())
    return digest.joinToString("") { "%02x".format(it) }
}