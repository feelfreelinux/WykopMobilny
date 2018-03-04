package io.github.feelfreelinux.wykopmobilny.api.embed

import io.reactivex.Single
import java.net.URL

interface EmbedApi {
    fun getGfycatMp4Url(gfycatId : String) : Single<URL>
    fun getGfycatWebmUrl(gfycatId: String) : Single<URL>
    fun getGifUrl(gfycatId: String) : Single<URL>
    fun getStreamableUrl(streamableId : String) : Single<URL>
}