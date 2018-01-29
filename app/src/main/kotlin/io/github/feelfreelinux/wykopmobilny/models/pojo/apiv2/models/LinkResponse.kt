package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

class LinkResponse(
        @Json(name="id")
        val id : Int,

        @Json(name="title")
        val title : String,

        @Json(name="description")
        val description : String?,

        @Json(name="tags")
        val tags : String,

        @Json(name="source_url")
        val sourceUrl : String,

        @Json(name="vote_count")
        val voteCount : Int,

        @Json(name="bury_count")
        val buryCount : Int,

        @Json(name="comments_count")
        val commentsCount : Int,

        @Json(name="related_count")
        val relatedCount : Int,

        @Json(name="author")
        val author : AuthorResponse?,

        @Json(name="date")
        val date : String,

        @Json(name="preview")
        val preview : String?,

        @Json(name="plus18")
        val plus18 : Boolean,

        @Json(name="can_vote")
        val canVote : Boolean,

        @Json(name="is_hot")
        val isHot : Boolean,

        @Json(name="status")
        val status : String,

        @Json(name="user_vote")
        val userVote : String?,

        @Json(name="user_favorite")
        val userFavorite : Boolean?,

        @Json(name="app")
        val app : String?
)