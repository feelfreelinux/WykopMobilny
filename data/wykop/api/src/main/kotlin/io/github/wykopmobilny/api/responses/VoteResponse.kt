package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VoteResponse(
    @field:Json(name = "vote_count") val voteCount: Int
)
