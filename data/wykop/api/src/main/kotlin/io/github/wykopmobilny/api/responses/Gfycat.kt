package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)

data class Gfycat(
    @JsonProperty("gfyItem") val gfyItem: GfycatItem
)