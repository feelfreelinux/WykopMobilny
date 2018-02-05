package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

class RelatedResponse (
        @Json(name="id")
        val id : Int,
        @Json(name="url")
        val url : String,
        @Json(name="vote_count")
        val voteCount : Int,
        @Json(name="author")
        val author : AuthorResponse?,
        @Json(name="title")
        val title : String,

        @Json(name="user_vote")
        val userVote : Int?
)