package io.github.feelfreelinux.wykopmobilny.api.embed

import io.reactivex.Single
import retrofit2.Retrofit

class EmbedRepository(val retrofit: Retrofit) : EmbedApi {
    override fun getGfycatMp4Url(gfycatId: String): Single<String> =
            embedApi.getGfycat(gfycatId)
                    .map { it.gfyItem.mp4Url }

    override fun getGfycatWebmUrl(gfycatId: String): Single<String> =
            embedApi.getGfycat(gfycatId)
                    .map { it.gfyItem.webmUrl }

    override fun getGifUrl(gfycatId: String): Single<String> =
            embedApi.getGfycat(gfycatId)
                    .map { it.gfyItem.gifUrl }

    override fun getStreamableUrl(streamableId: String): Single<String> =
            embedApi.getStreamableFile(streamableId)
                    .map { it.files.mp4Mobile.url }

    private val embedApi by lazy { retrofit.create(EmbedRetrofitApi::class.java) }
}