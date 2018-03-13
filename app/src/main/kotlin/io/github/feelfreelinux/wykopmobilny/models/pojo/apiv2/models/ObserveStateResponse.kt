package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

class ObserveStateResponse(
        @JsonProperty("is_observed")
        val isObserved : Boolean,
        @JsonProperty("is_blocked")
        val isBlocked : Boolean
)