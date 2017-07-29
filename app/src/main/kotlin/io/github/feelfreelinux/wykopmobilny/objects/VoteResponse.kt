package io.github.feelfreelinux.wykopmobilny.objects

import com.google.gson.annotations.SerializedName
import java.util.*


data class VoteResponse(
        val voteCount: Int,
        val voters: List<Voter>)

data class Voter(
        val author: String,
        val authorauthor_group: Int,
        val author_avatar: String,
        val authorAvatarBig: String,
        val authorAvatarMed: String,
        val authorAvatarLo: String,
        val authorSex: String,
        val date: String

)