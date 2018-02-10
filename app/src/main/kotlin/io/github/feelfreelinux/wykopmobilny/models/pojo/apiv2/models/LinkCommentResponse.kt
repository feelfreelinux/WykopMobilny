package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class LinkCommentResponse(
        @Json(name = "id")
        val id : Int,
        @Json(name = "author")
        val author : AuthorResponse,
        @Json(name = "date")
        val date : String,
        @Json(name = "body")
        val body : String?,
        @Json(name = "blocked")
        val blocked : Boolean,
        @Json(name = "favorite")
        val favorite : Boolean,
        @Json(name = "vote_count")
        val voteCount : Int,
        @Json(name = "vote_count_plus")
        val voteCountPlus : Int,
        @Json(name = "user_vote")
        val userVote : Int,
        @Json(name = "parent_id")
        val parentId : Int,
        @Json(name = "can_vote")
        val canVote : Boolean,
        @Json(name = "link_id")
        val linkId : Int,
        @Json(name = "embed")
        val embed : EmbedResponse?,
        @Json(name="violation_url")
        val violationUrl : String?,
        @Json(name = "app")
        val app : String?
)