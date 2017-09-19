package io.github.feelfreelinux.wykopmobilny.api

import com.squareup.moshi.Json
import java.io.Serializable

data class Author(val nick : String,
                  val avatarUrl : String,
                  val group: Int,
                  val sex : String,
                  val app : String?)

data class Entry(val id : Int,
                 val author : Author,
                 val body : String,
                 val date : String,
                 val isVoted : Boolean,
                 val embed : Embed?,
                 val voteCount : Int,
                 val commentsCount : Int,
                 val comments : List<Comment>)

data class Comment(val id : Int,
                   val entryId : Int,
                   val author: Author,
                   val body: String,
                   val date : String,
                   val isVoted : Boolean,
                   val embed : Embed?,
                   val voteCount: Int)

data class Embed(val type : String,
                 val preview : String,
                 val url : String,
                 val plus18 : Boolean,
                 val source : String)

data class TagMeta(val tag: String,
                   val isObserved: Boolean,
                   val isBlocked: Boolean)

data class TagEntries(val meta : TagMeta,
                      val entries : List<Entry>)