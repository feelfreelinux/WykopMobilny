package io.github.feelfreelinux.wykopmobilny.utils

import android.graphics.Color
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.*
import kotlinx.android.synthetic.main.feed_layout.view.*
import org.json.JSONObject
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
        1001 -> loginColor = Color.parseColor("#3F6FA0")
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