package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty

data class AddLinkPreviewImage(
        @JsonProperty("key")
        val key : String,

        @JsonProperty("type")
        val type : String,

        @JsonProperty("preview_url")
        val previewUrl : String,

        @JsonProperty("source_url")
        val sourceUrl : String
)