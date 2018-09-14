package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class StreamableFile(
    @JsonProperty("url") val url: String
)