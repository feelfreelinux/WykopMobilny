package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class TagSuggestionResponse(
        @JsonProperty("tag")
        val tag : String,

        @JsonProperty("followers")
        val followers : Int
)