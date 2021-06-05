package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class WykopMobilnyApk(
    @JsonProperty("content_type") val contentType: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("browser_download_url") val browserDownloadUrl: String
)
