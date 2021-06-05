package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Related(
    val id: Int,
    val url: String,
    var voteCount: Int,
    val author: Author?,
    val title: String,
    var userVote: Int
)
