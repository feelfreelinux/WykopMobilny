package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class WykopMobilnyUpdate(
    @JsonProperty("tag_name") val tagName: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("prerelease") val isPrelease: Boolean,
    @JsonProperty("assets") val assets: List<WykopMobilnyApk>,
    @JsonProperty("published_at") val publishedAt: String,
    @JsonProperty("body") val body: String
)
