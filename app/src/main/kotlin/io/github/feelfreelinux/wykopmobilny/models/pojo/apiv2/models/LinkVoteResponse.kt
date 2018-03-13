package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)

class LinkVoteResponse(
        @JsonProperty("vote_count_plus")
        val voteCountPlus : Int,

        @JsonProperty("vote_count")
        val voteCount : Int
) { val voteCountMinus : Int get() = voteCount - voteCountPlus }