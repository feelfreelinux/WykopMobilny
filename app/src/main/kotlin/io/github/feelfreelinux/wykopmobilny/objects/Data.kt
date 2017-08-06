package io.github.feelfreelinux.wykopmobilny.objects

import java.io.Serializable
import java.util.*

data class Embed(
        val type : String,
        val preview : String,
        val url : String,
        val plus18 : Boolean,
        val source : String) : Serializable

data class Entries(
        val id: Int,
        val author: User,
        var votes_count: Int,
        val comments_count: Int?,
        val body: String,
        val app: String?,
        val date: Date,
        val embed: Embed?,
        var voted: Boolean,
        val rawData: Any?)

data class User(
        val avatarUrl : String,
        val role : Int,
        val nick : String,
        val gender : String) : Serializable