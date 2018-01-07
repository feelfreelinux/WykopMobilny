package io.github.feelfreelinux.wykopmobilny.models.dataclass

data class PMMessage(
        val date : String,
        val body: String,
        val embed: Embed?,
        val isSentFromUser : Boolean,
        val app : String?
)