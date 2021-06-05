package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty

data class NotificationsCountResponse(
    @JsonProperty("count") val count: Int
)
