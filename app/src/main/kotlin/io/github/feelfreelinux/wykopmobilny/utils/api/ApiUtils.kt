package io.github.feelfreelinux.wykopmobilny.utils.api

import android.graphics.Color
import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.R
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

fun parseDate(date : String) : Date = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.GERMANY).parse(date)

fun getGroupColor(role : Int) : Int {
    var loginColor = Color.BLUE
    when(role) {
        0 -> loginColor = Color.parseColor("#339933")
        1 -> loginColor = Color.parseColor("#ff5917")
        2 -> loginColor = Color.parseColor("#BB0000")
        5 -> loginColor = Color.parseColor("#ffffff")
        1001 -> loginColor = Color.parseColor("#999999")
        1002 -> loginColor = Color.parseColor("#999999")
        2001 -> loginColor = Color.parseColor("#3F6FA0")
    }
    return loginColor
}

fun getGenderStripResource(authorSex : String) : Int {
    when (authorSex) {
        "male" -> return R.drawable.male_strip
        "female" -> return R.drawable.female_strip
        else -> return 0
    }
}

fun String.encryptMD5() : String{
    val bytes = toByteArray()
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    var result = ""
    for (byte in digest) result += "%02x".format(byte)
    return result
}

fun Uri.getWpisId(): Int {
    val subUrl = toString().substringAfter("/wpis/")
    return subUrl.substringBefore("/").toInt()
}