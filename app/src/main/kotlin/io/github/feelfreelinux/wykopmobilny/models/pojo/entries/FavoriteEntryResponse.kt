package io.github.feelfreelinux.wykopmobilny.models.pojo.entries

import com.squareup.moshi.Json

data class FavoriteEntryResponse(
        @Json(name="user_favorite")
        val userFavorite : Boolean
)