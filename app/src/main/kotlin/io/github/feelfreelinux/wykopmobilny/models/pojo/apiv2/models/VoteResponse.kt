package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class VoteResponse(
        @Json(name="vote_count")
        val voteCount : Int
)