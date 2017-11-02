package io.github.feelfreelinux.wykopmobilny.models.pojo

import com.squareup.moshi.Json

data class NotificationResponse(
        @Json(name = "author")
        val author : String?,

        @Json(name = "author_avatar")
        val authorAvatar : String?,

        @Json(name = "author_avatar_med")
        val authorAvatarMed : String?,

        @Json(name = "author_avatar_lo")
        val authorAvatarLo : String?,

        @Json(name = "author_group")
        val authorGroup : Int?,

        @Json(name = "author_sex")
        val authorSex : String?,

        @Json(name = "date")
        val date : String,

        @Json(name = "type")
        val type : String,

        @Json(name = "body")
        val body : String,

        @Json(name = "url")
        val url : String,

        @Json(name = "new")
        val new : Boolean
)