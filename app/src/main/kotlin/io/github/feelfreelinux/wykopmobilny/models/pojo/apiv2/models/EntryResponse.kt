package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

class EntryResponse(
        @Json(name="id")
        val id : Int,
        @Json(name="date")
        val date : String,

        @Json(name="body")
        val body : String?,

        @Json(name="author")
        val author : AuthorResponse,
        @Json(name="blocked")
        val blocked : Boolean,
        @Json(name="favorite")
        val favorite : Boolean,
        @Json(name="vote_count")
        val voteCount : Int,
        @Json(name="comments_count")
        val commentsCount : Int,

        @Json(name="comments")
        val comments : List<EntryCommentResponse>?,

        @Json(name="status")
        val status : String,

        @Json(name="embed")
        val embed : EmbedResponse?,

        @Json(name="survey")
        val survey : SurveyResponse?,

        @Json(name="user_vote")
        val userVote : Int,

        @Json(name="violation_url")
        val violationUrl : String?,

        @Json(name="app")
        val app : String?
)