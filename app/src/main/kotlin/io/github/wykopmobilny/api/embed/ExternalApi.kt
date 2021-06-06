package io.github.wykopmobilny.api.embed

import io.github.wykopmobilny.api.responses.Coub
import io.github.wykopmobilny.api.responses.Gfycat
import io.reactivex.Single
import java.net.URL

interface ExternalApi {
    fun getCoub(coubId: String): Single<Coub>
    fun getGfycat(gfycatId: String): Single<Gfycat>
    fun getStreamableUrl(streamableId: String): Single<URL>
}
