package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.REMOVE_USERKEY_HEADER
import io.github.wykopmobilny.api.responses.Coub
import io.github.wykopmobilny.api.responses.Gfycat
import io.github.wykopmobilny.api.responses.Streamable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ExternalRetrofitApi {

    @Headers("@: $REMOVE_USERKEY_HEADER")
    @GET("https://api.gfycat.com/v1/gfycats/{gfycatId}")
    fun getGfycat(@Path("gfycatId") gfycatItem: String): Single<Gfycat>

    @Headers("@: $REMOVE_USERKEY_HEADER")
    @GET("https://api.streamable.com/videos/{streamableId}")
    fun getStreamableFile(@Path("streamableId") streamableId: String): Single<Streamable>

    @Headers("@: $REMOVE_USERKEY_HEADER")
    @GET("https://coub.com/api/v2/coubs/{coubId}")
    fun getCoub(@Path("coubId") coubId: String): Single<Coub>
}
