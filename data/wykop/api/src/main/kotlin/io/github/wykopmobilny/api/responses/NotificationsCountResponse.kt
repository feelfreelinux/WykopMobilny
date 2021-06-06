package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationsCountResponse(
    @JsonProperty("count") val count: Int
)
