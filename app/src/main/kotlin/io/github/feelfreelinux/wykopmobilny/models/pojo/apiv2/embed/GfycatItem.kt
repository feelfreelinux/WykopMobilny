package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class GfycatItem(
        @JsonProperty("mp4Url")
        val mp4Url : String,
        @JsonProperty("gifUrl")
        val gifUrl : String,
        @JsonProperty("webmUrl")
        val webmUrl : String
)
