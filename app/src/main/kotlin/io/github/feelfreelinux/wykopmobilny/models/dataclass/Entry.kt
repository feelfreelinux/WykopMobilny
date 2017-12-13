package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class Entry(val id : Int,
                 val author : Author,
                 val body : String,
                 val date : String,
                 var isVoted : Boolean,
                 var isFavorite : Boolean,
                 val survey : Survey?,
                 val embed : Embed?,
                 var voteCount : Int,
                 val commentsCount : Int,
                 val comments : List<EntryComment>,
                 val app : String?)