package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.fasterxml.jackson.annotation.JsonProperty import com.fasterxml.jackson.annotation.JsonIgnoreProperties @JsonIgnoreProperties(ignoreUnknown = true)

data class SurveyResponse(
        @JsonProperty("question")
        val question : String,

        @JsonProperty("answers")
        val answers : List<AnswerResponse>,

        @JsonProperty("user_answer")
        val userAnswer : Int?
)