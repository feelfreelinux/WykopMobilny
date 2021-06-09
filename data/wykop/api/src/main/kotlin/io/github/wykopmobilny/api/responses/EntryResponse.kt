package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EntryResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "body") val body: String?,
    @field:Json(name = "author") val author: AuthorResponse,
    @field:Json(name = "blocked") val blocked: Boolean,
    @field:Json(name = "favorite") val favorite: Boolean,
    @field:Json(name = "vote_count") val voteCount: Int,
    @field:Json(name = "comments_count") val commentsCount: Int,
    @field:Json(name = "comments") val comments: List<EntryCommentResponse>?,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "embed") val embed: EmbedResponse?,
    @field:Json(name = "survey") val survey: SurveyResponse?,
    @field:Json(name = "user_vote") val userVote: Int,
    @field:Json(name = "violation_url") val violationUrl: String?,
    @field:Json(name = "app") val app: String?,
    @field:Json(name = "can_comment") val isCommentingPossible: Boolean?
)
