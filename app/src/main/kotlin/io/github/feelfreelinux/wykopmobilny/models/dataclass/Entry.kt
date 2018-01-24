package io.github.feelfreelinux.wykopmobilny.models.dataclass

class Entry(val id : Int,
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
                 val app : String?) {
    override fun equals(other: Any?): Boolean {
        return if (other !is Entry) false
        else (other.id == id)
    }

    override fun hashCode(): Int {
        return id
    }
}