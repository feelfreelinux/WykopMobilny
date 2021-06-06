package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonProperty

class NewLinkInformationResponse(
    @JsonProperty("key") val key: String,
    @JsonProperty("title") val title: String? = "",
    @JsonProperty("description") val description: String? = "",
    @JsonProperty("source_url") val sourceUrl: String
)
