package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json
import kotlin.math.absoluteValue

class LinkVoteResponse(
        @Json(name = "vote_count_plus")
        val voteCountPlus : Int,

        @Json(name = "vote_count")
        val voteCount : Int
) { val voteCountMinus : Int get() = voteCount - voteCountPlus }