package io.github.feelfreelinux.wykopmobilny.objects

import java.util.*

class Entry(
        val id : Int,
        val author : User,
        var votes_count : Int,
        val comments_count : Int,
        val body : String,
        val date : Date,
        val embed: Embed,
        var isComment : Boolean,
        var voted : Boolean,
        var entryId : Int?)