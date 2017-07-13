package io.github.feelfreelinux.wykopmobilny

import android.graphics.Color
import io.github.feelfreelinux.wykopmobilny.objects.Embed
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.User
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun parseEntry(json : JSONObject) : Entry {
    var embed = Embed("null", "null", "null", "null", false)
    if (!json.isNull("embed")) embed = parseEmbed(json.getJSONObject("embed"))
    return Entry(
            json.getInt("id"),
            User(
                    json.getString("author_avatar_med"),
                    json.getInt("author_group"),
                    json.getString("author"),
                    json.getString("author_sex")),
            json.getInt("vote_count"),
            json.getInt("comment_count"),
            json.getString("body"),
            parseDate(
                    json.getString("date")
            ),
            embed
    )
}

fun parseEmbed(json : JSONObject) : Embed {
    return Embed(
            json.getString("type"),
            json.getString("preview"),
            json.getString("url"),
            json.getString("source"),
            json.getBoolean("plus18")
    )
}

fun parseDate(date : String) : Date = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.GERMANY).parse(date)

fun getGroupColor(role : Int) : Int {
    var loginColor = Color.BLUE
    when(role) {
        0 -> loginColor = Color.parseColor("#339933")
        1 -> loginColor = Color.parseColor("#ff5917")
        2 -> loginColor = Color.parseColor("#BB0000")
        3 -> loginColor = Color.parseColor("#ff0000")
        4 -> loginColor = Color.parseColor("#999999")
        5 -> loginColor = Color.parseColor("#fff")
        6 -> loginColor = Color.parseColor("#367aa9")
    }
    return loginColor
}