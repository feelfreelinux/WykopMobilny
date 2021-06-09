package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RelatedResponse(
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "vote_count") val voteCount: Int,
    @field:Json(name = "author") val author: AuthorResponse?,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "user_vote") val userVote: Int?
)
