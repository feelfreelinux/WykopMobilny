package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class ProfileResponse(
        @Json(name="login")
        val login  : String? = null,

        @Json(name="color")
        val color : Int?= 0,

        @Json(name="sex")
        val sex : String? = null,

        @Json(name="avatar")
        val avatar : String? = null,

        @Json(name="signup_at")
        val signupAt : String? = null,
        @Json(name="is_blocked")
        val isBlocked : Boolean? = false,
        @Json(name="is_observed")
        val isObserved : Boolean? = false,
        @Json(name="background")
        val background : String? = null,

        @Json(name="links_added_count")
        val linksAddedCount : Int?=0,

        @Json(name="links_published_count")
        val linksPublishedCount : Int?=0,

        @Json(name="rank")
        val rank : Int?=0,

        @Json(name="followers")
        val followers : Int?=0,
        @Json(name="about")
        val description : String? = null,
        @Json(name="violation_url")
        val violationUrl : String? = null
)