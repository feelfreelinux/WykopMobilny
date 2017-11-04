package io.github.feelfreelinux.wykopmobilny.models.dataclass

import retrofit2.http.Body

data class PMMessage(
        val date : String,
        val body: String,
        val embed: Embed?,
        val isSentFromUser : Boolean
)