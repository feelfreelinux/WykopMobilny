package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class ProfileResponse(
        @Json(name="login")
        val login  : String,

        @Json(name="color")
        val color : Int,

        @Json(name="sex")
        val sex : String,

        @Json(name="avatar")
        val avatar : String,

        @Json(name="signup_at")
        val signupAt : String,
        @Json(name="is_blocked")
        val isBlocked : Boolean? = false,
        @Json(name="is_observed")
        val isObserved : Boolean? = false,
        @Json(name="background")
        val background : String?,

        @Json(name="links_added_count")
        val linksAddedCount : Int,

        @Json(name="links_published_count")
        val linksPublishedCount : Int,

        @Json(name="rank")
        val rank : Int?,

        @Json(name="followers")
        val followers : Int,
        @Json(name="about")
        val description : String?
)