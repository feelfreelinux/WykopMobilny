package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses

import com.fasterxml.jackson.annotation.JsonProperty

class NewLinkInformationResponse(
    @JsonProperty("key") val key: String,
    @JsonProperty("title") val title: String? = "",
    @JsonProperty("description") val description: String? = "",
    @JsonProperty("source_url") val sourceUrl: String
)