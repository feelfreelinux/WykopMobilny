package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GfycatItem(
    @JsonProperty("mp4Url") val mp4Url: String,
    @JsonProperty("gifUrl") val gifUrl: String,
    @JsonProperty("webmUrl") val webmUrl: String
)
