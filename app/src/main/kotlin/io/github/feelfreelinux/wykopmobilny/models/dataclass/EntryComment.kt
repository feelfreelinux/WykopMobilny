package io.github.feelfreelinux.wykopmobilny.models.dataclass

class EntryComment(val id : Int,
                        var entryId : Int,
                        val author: Author,
                        val body: String,
                        val date : String,
                        var isVoted : Boolean,
                        val embed : Embed?,
                        var voteCount: Int,
                        val app : String?,
                        var isNsfw : Boolean = false) {
    override fun equals(other: Any?): Boolean {
        return if (other !is EntryComment) false
        else (other.id == id)
    }

    override fun hashCode(): Int {
        return id
    }
}