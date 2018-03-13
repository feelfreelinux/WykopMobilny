package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class VoterResponse(
        @JsonProperty("author")
        val author : AuthorResponse,
        @JsonProperty("date")
        val date : String,
        @JsonProperty("vote_type")
        val voteType : Int
)