package io.github.feelfreelinux.wykopmobilny.utils

import java.security.MessageDigest

fun String.encryptMD5() : String{
    val bytes = toByteArray()
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    var result = ""
    for (byte in digest) result += "%02x".format(byte)
    return result
}