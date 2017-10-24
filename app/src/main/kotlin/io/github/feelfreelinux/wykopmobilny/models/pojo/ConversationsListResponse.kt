package io.github.feelfreelinux.wykopmobilny.models.pojo

import com.squareup.moshi.Json

data class ConversationsListResponse(
        @Json(name = "last_update")
        val lastUpdate : String,

        @Json(name = "author")
        val author : String,

        @Json(name = "author_avatar")
        val authorAvatar : String,

        @Json(name = "author_avatar_med")
        val authorAvatarMed : String,

        @Json(name = "author_avatar_lo")
        val authorAvatarLo : String,

        @Json(name = "author_group")
        val authorGroup : Int,

        @Json(name = "author_sex")
        val authorSex : String,

        @Json(name = "status")
        val status : String
)