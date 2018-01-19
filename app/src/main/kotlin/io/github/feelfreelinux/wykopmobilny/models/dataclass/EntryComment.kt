package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class EntryComment(val id : Int,
                        var entryId : Int,
                        val author: Author,
                        val body: String,
                        val date : String,
                        var isVoted : Boolean,
                        val embed : Embed?,
                        var voteCount: Int,
                        val app : String?)