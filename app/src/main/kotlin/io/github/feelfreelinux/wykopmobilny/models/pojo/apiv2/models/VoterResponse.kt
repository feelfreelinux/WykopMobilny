package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class VoterResponse(
        @Json(name="author")
        val author : AuthorResponse,
        @Json(name="date")
        val date : String,
        @Json(name="vote_type")
        val voteType : Int
)