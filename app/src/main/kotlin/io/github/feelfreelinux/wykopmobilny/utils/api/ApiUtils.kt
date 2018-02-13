package io.github.feelfreelinux.wykopmobilny.utils.api

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.TypedValue
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.printout
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

fun parseDate(date : String) : Date {
    val format = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.GERMANY)
    format.timeZone = TimeZone.getTimeZone("Europe/Warsaw")
    return format.parse(date)
}

fun parseDateJavaTime(date : String) : LocalDate {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss", Locale.GERMANY)
    return LocalDate.parse(date, format.withZone(ZoneId.of("Europe/Warsaw")))
}

fun getGroupColor(role : Int, isUsingDarkTheme : Boolean = true) : Int = when(role) {
    0 -> Color.parseColor("#339933")
    1 -> Color.parseColor("#ff5917")
    2 -> Color.parseColor("#BB0000")
    5 ->
        if (isUsingDarkTheme) Color.parseColor("#ffffff")
        else Color.parseColor("#000000")
    1001 -> Color.parseColor("#999999")
    1002 -> Color.parseColor("#999999")
    2001 -> Color.parseColor("#3F6FA0")
    else -> Color.BLUE
}

fun Context.getGroupColor(role : Int) : Int = when(role) {
    0 -> Color.parseColor("#339933")
    1 -> Color.parseColor("#ff5917")
    2 -> Color.parseColor("#BB0000")
    5 -> {
        val tv = TypedValue()
        theme.resolveAttribute(R.attr.adminNickColor, tv, true)
        tv.data

    }
    1001 -> Color.parseColor("#999999")
    1002 -> Color.parseColor("#999999")
    2001 -> Color.parseColor("#3F6FA0")
    else -> Color.BLUE
}

fun getGenderStripResource(authorSex : String) : Int =
    when (authorSex) {
        "male" -> R.drawable.male_strip
        "female" -> R.drawable.female_strip
        else -> 0
    }

fun String.stripImageCompression() : String {
    val extension = substringAfterLast(".")
    val baseUrl = substringBeforeLast(",")
    return baseUrl + if (!baseUrl.endsWith(extension)) "." + extension else ""
}

fun String.encryptMD5() : String{
    val bytes = toByteArray()
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    var result = ""
    for (byte in digest) result += "%02x".format(byte)
    return result
}

fun Uri.getTag(): String {
    var subUrl = toString().substringAfter("/tag/")
    if (subUrl.last() == '/') subUrl = subUrl.substring(0, subUrl.length - 1)
    printout(subUrl)
    return subUrl.substringAfterLast("/")
}