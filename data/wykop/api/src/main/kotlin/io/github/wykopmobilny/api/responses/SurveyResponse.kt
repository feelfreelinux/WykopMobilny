package io.github.wykopmobilny.api.responses

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SurveyResponse(
    @JsonProperty("question") val question: String?,
    @JsonProperty("answers") val answers: List<AnswerResponse>,
    @JsonProperty("user_answer") val userAnswer: Int?
)
