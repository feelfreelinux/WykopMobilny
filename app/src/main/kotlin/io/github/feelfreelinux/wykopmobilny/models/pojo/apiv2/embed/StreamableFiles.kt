package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class StreamableFiles(
        @JsonProperty("mp4")
        val mp4 : StreamableFile,
        @JsonProperty("mp4-mobile")
        val mp4Mobile : StreamableFile?
)
