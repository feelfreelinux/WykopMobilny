package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinkVoteResponse(
    @field:Json(name = "vote_count_plus") val voteCountPlus: Int,
    @field:Json(name = "vote_count") val voteCount: Int
) {
    val voteCountMinus: Int get() = voteCount - voteCountPlus
}
