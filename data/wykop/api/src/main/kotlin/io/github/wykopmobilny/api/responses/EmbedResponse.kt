package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class EmbedResponse(
    @JsonProperty("type") val type: String,
    @JsonProperty("url") val url: String,
    @JsonProperty("source") val source: String?,
    @JsonProperty("preview") val preview: String,
    @JsonProperty("plus18") val plus18: Boolean,
    @JsonProperty("size") val size: String?,
    @JsonProperty("animated") val animated: Boolean
)
