package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class ConversationResponse(
        @JsonProperty("last_update")
        val lastUpdate : String,

        @JsonProperty("receiver")
        val receiver : AuthorResponse,

        @JsonProperty("status")
        val status : String
)