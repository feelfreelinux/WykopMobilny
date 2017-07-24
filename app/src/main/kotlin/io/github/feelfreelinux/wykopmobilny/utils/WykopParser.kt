package io.github.feelfreelinux.wykopmobilny.utils

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
    var voted = false
    if (json.getInt("user_vote") > 0) voted = true
    var comment_count = 0
    if (json.has("comment_count")) comment_count = json.getInt("comment_count")
    return Entry(
            json.getInt("id"),
            User(
                    json.getString("author_avatar_med"),
                    json.getInt("author_group"),
                    json.getString("author"),
                    json.getString("author_sex")),
            json.getInt("vote_count"),
            comment_count,
            json.getString("body"),
            parseDate(
                    json.getString("date")
            ),
            embed,
            false,
            voted,
            null
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
        5 -> loginColor = Color.parseColor("#ffffff")
        6 -> loginColor = Color.parseColor("#367aa9")
    }
    return loginColor
}