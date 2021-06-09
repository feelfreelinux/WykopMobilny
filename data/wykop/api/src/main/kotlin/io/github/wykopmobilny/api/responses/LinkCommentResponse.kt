package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkCommentResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "author") val author: AuthorResponse?,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "body") val body: String?,
    @field:Json(name = "blocked") val blocked: Boolean,
    @field:Json(name = "favorite") val favorite: Boolean,
    @field:Json(name = "vote_count") val voteCount: Int,
    @field:Json(name = "vote_count_plus") val voteCountPlus: Int,
    @field:Json(name = "user_vote") val userVote: Int,
    @field:Json(name = "parent_id") val parentId: Int,
    @field:Json(name = "can_vote") val canVote: Boolean,
    @field:Json(name = "link_id") val linkId: Int,
    @field:Json(name = "embed") val embed: EmbedResponse?,
    @field:Json(name = "violation_url") val violationUrl: String?,
    @field:Json(name = "app") val app: String?
)
