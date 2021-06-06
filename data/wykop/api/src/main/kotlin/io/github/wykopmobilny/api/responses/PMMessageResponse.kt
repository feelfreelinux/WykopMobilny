package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PMMessageResponse(
    @JsonProperty("id") val id: Int,
    @JsonProperty("date") val date: String,
    @JsonProperty("body") val body: String?,
    @JsonProperty("status") val status: String,
    @JsonProperty("direction") val direction: String,
    @JsonProperty("embed") val embed: EmbedResponse?,
    @JsonProperty("app") val app: String?
)
