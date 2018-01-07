package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class SurveyResponse(
        @Json(name="question")
        val question : String,

        @Json(name="answers")
        val answers : List<AnswerResponse>,

        @Json(name="user_answer")
        val userAnswer : Int?
)