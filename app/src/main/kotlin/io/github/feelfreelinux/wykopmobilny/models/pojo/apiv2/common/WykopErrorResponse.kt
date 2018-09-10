package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

class WykopErrorResponse(
        @JsonProperty("code")
        val code : Int,
        @JsonProperty("message_pl")
        val messagePl : String
)