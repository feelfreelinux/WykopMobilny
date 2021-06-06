package io.github.wykopmobilny.api.embed

import io.github.wykopmobilny.api.endpoints.ExternalRetrofitApi
import java.net.URL
import javax.inject.Inject

class ExternalRepository @Inject constructor(
    private val embedApi: ExternalRetrofitApi,
) : ExternalApi {

    override fun getGfycatMp4Url(gfycatId: String) =
        embedApi.getGfycat(gfycatId).map { URL(it.gfyItem.mp4Url) }

    override fun getGfycatWebmUrl(gfycatId: String) =
        embedApi.getGfycat(gfycatId).map { URL(it.gfyItem.webmUrl) }

    override fun getGifUrl(gfycatId: String) =
        embedApi.getGfycat(gfycatId).map { URL(it.gfyItem.gifUrl) }

    override fun getStreamableUrl(streamableId: String) =
        embedApi.getStreamableFile(streamableId)
            .map { URL(it.files.mp4Mobile?.url ?: it.files.mp4.url) }

    override fun getCoub(coubId: String) =
        embedApi.getCoub(coubId)

    override fun getGfycat(gfycatId: String) =
        embedApi.getGfycat(gfycatId)
}
