package io.github.wykopmobilny.api.embed

import io.github.wykopmobilny.api.endpoints.ExternalRetrofitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.rxSingle
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject

class ExternalRepository @Inject constructor(
    private val embedApi: ExternalRetrofitApi,
) : ExternalApi {

    override fun getStreamableUrl(streamableId: String) =
        rxSingle {
            val streamable = embedApi.getStreamableFile(streamableId)

            withContext(Dispatchers.Default) {
                URL(streamable.files.mp4Mobile?.url ?: streamable.files.mp4.url)
            }
        }

    override fun getCoub(coubId: String) =
        rxSingle { embedApi.getCoub(coubId) }

    override fun getGfycat(gfycatId: String) =
        rxSingle { embedApi.getGfycat(gfycatId) }
}
