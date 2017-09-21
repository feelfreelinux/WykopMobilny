package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Entry(val id : Int,
                 val author : Author,
                 val body : String,
                 val date : String,
                 val isVoted : Boolean,
                 val embed : Embed?,
                 val voteCount : Int,
                 val commentsCount : Int,
                 val comments : List<Comment>)