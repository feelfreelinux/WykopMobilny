package io.github.wykopmobilny.models.dataclass

data class Survey(
    val question: String,
    val answers: List<Answer>,
    val userAnswer: Int?
)
