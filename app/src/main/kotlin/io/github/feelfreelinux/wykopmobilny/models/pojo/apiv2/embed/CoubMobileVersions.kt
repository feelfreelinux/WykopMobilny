package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CoubMobileVersions(
    @JsonProperty("video") val mp4: String?,
    @JsonProperty("audio") val audio: List<String>
)