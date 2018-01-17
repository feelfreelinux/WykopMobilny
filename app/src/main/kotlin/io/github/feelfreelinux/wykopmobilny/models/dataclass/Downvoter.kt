package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Downvoter(
        val author: Author,
        val date : String,
        val reason : Int
)