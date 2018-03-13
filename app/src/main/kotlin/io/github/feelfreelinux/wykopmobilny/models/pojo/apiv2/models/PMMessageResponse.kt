package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class PMMessageResponse(
        @JsonProperty("id")
        val id : Int,

        @JsonProperty("date")
        val date : String,

        @JsonProperty("body")
        val body : String?,

        @JsonProperty("status")
        val status : String,

        @JsonProperty("direction")
        val direction : String,

        @JsonProperty("embed")
        val embed : EmbedResponse?,

        @JsonProperty("app")
        val app : String?
)