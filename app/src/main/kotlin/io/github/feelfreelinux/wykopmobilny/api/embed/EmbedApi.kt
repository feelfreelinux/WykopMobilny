package io.github.feelfreelinux.wykopmobilny.api.embed

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Coub
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Gfycat
import io.reactivex.Single
import java.net.URL

interface EmbedApi {
    fun getGfycatMp4Url(gfycatId : String) : Single<URL>
    fun getGfycatWebmUrl(gfycatId: String) : Single<URL>
    fun getGifUrl(gfycatId: String) : Single<URL>
    fun getCoub(coubId : String) : Single<Coub>
    fun getGfycat(gfycatId : String) : Single<Gfycat>

    fun getStreamableUrl(streamableId : String) : Single<URL>
}