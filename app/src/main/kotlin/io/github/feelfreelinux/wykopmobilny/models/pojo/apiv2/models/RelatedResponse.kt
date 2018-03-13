package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

class RelatedResponse (
        @JsonProperty("id")
        val id : Int,
        @JsonProperty("url")
        val url : String,
        @JsonProperty("vote_count")
        val voteCount : Int,
        @JsonProperty("author")
        val author : AuthorResponse?,
        @JsonProperty("title")
        val title : String,

        @JsonProperty("user_vote")
        val userVote : Int?
)