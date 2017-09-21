package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Comment(val id : Int,
                   val entryId : Int,
                   val author: Author,
                   val body: String,
                   val date : String,
                   val isVoted : Boolean,
                   val embed : Embed?,
                   val voteCount: Int)