package io.github.feelfreelinux.wykopmobilny.utils

import android.graphics.Color
import io.github.feelfreelinux.wykopmobilny.objects.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

fun parseEntry(entry: SingleEntry) : Entry {
    return Entry(
            entry.id,
            User(
                    entry.author_avatar_med,
                    entry.author_group,
                    entry.author,
                    entry.author_sex
            ),
            entry.vote_count,
            entry.comment_count,
            entry.body,
            entry.app,
            parseDate(
                    entry.date
            ),
            entry.embed,
            (entry.user_vote > 0),
            entry
    )
}

fun parseEntry(entry: Comment) : Entry {
    return Entry(
            entry.id,
            User(
                    entry.author_avatar_med,
                    entry.author_group,
                    entry.author,
                    entry.author_sex
            ),
            entry.vote_count,
            null,
            entry.body,
            entry.app,
            parseDate(
                    entry.date
            ),
            entry.embed,
            (entry.user_vote > 0),
            entry
    )
}

fun parseEntry(entry: EntryDetails) : Entry {
    return Entry(
            entry.id,
            User(
                    entry.author_avatar_med,
                    entry.author_group,
                    entry.author,
                    entry.author_sex
            ),
            entry.vote_count,
            entry.comment_count,
            entry.body,
            entry.app,
            parseDate(
                    entry.date
            ),
            entry.embed,
            (entry.user_vote > 0),
            entry
    )
}

fun parseSingleEntryList(arr : Array<SingleEntry>) : List<Entry> = arr.map { parseEntry(it) }


fun parseDate(date : String) : Date = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.GERMANY).parse(date)

fun parseEntryUser(json : JSONObject) : User = User(
        json.getString("author_avatar_med"),
        json.getInt("author_group"),
        json.getString("author"),
        json.getString("author_sex"))

fun parseUser(json : JSONObject) : User = User(
        json.getString("avatar_big"),
        json.getInt("author_group"),
        json.getString("login"),
        json.getString("sex"))

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