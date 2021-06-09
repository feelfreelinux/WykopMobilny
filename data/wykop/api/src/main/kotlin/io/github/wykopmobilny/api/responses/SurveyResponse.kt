package io.github.wykopmobilny.api.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SurveyResponse(
    @field:Json(name = "question") val question: String?,
    @field:Json(name = "answers") val answers: List<AnswerResponse>,
    @field:Json(name = "user_answer") val userAnswer: Int?
)
