package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AnswerResponse(
    @JsonProperty("id") val id: Int,
    @JsonProperty("answer") val answer: String,
    @JsonProperty("count") val count: Int,
    @JsonProperty("percentage") val percentage: Double
)
