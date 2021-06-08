package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EntryCommentResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "entry_id") val entryId: Int?,
    @field:Json(name = "author") val author: AuthorResponse,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "body") val body: String?,
    @field:Json(name = "blocked") val blocked: Boolean,
    @field:Json(name = "favorite") val favorite: Boolean,
    @field:Json(name = "vote_count") val voteCount: Int,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "user_vote") val userVote: Int,
    @field:Json(name = "embed") val embed: EmbedResponse?,
    @field:Json(name = "app") val app: String?,
    @field:Json(name = "violation_url") val violationUrl: String?
)
