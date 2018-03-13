package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class NotificationResponse(
        @JsonProperty("id")
        val id : Int,
        @JsonProperty("author")
        val author : AuthorResponse?,
        @JsonProperty("date")
        val date : String,
        @JsonProperty("body")
        val body : String,
        @JsonProperty("type")
        val type : String,
        @JsonProperty("item_id")
        val itemId : String?,
        @JsonProperty("url")
        val url : String?,
        @JsonProperty("new")
        val new : Boolean
)