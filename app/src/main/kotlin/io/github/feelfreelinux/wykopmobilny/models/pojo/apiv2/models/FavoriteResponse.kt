package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models

import com.squareup.moshi.Json

data class FavoriteResponse(
        @Json(name="user_favorite")
        val userFavorite : Boolean
)