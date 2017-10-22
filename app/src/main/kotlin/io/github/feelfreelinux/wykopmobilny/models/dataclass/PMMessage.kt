package io.github.feelfreelinux.wykopmobilny.models.dataclass

import retrofit2.http.Body

data class PMMessage(
        val author: Author,
        val date : String,
        val body: String,
        val embed: Embed?,
        val isNew : Boolean,
        val isSentFromUser : Boolean
)