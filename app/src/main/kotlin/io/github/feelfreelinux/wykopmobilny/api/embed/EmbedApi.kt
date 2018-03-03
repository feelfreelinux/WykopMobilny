package io.github.feelfreelinux.wykopmobilny.api.embed

import io.reactivex.Single

interface EmbedApi {
    fun getGfycatMp4Url(gfycatId : String) : Single<String>
    fun getGfycatWebmUrl(gfycatId: String) : Single<String>
    fun getGifUrl(gfycatId: String) : Single<String>
    fun getStreamableUrl(streamableId : String) : Single<String>
}