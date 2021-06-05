package io.github.feelfreelinux.wykopmobilny.api.embed

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Coub
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Gfycat
import io.reactivex.Single
import retrofit2.Retrofit
import java.net.URL

class ExternalRepository(val retrofit: Retrofit) : ExternalApi {

    private val embedApi by lazy { retrofit.create(ExternalRetrofitApi::class.java) }

    override fun getGfycatMp4Url(gfycatId: String): Single<URL> =
        embedApi.getGfycat(gfycatId).map { URL(it.gfyItem.mp4Url) }

    override fun getGfycatWebmUrl(gfycatId: String): Single<URL> =
        embedApi.getGfycat(gfycatId).map { URL(it.gfyItem.webmUrl) }

    override fun getGifUrl(gfycatId: String): Single<URL> =
        embedApi.getGfycat(gfycatId).map { URL(it.gfyItem.gifUrl) }

    override fun getStreamableUrl(streamableId: String): Single<URL> =
        embedApi.getStreamableFile(streamableId)
            .map { URL(it.files.mp4Mobile?.url ?: it.files.mp4.url) }

    override fun getCoub(coubId: String): Single<Coub> =
        embedApi.getCoub(coubId)

    override fun getGfycat(gfycatId: String): Single<Gfycat> =
        embedApi.getGfycat(gfycatId)
}
