package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Answer(
    val id: Int,
    val answer: String,
    val count: Int,
    val percentage: Double
)