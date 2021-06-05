package io.github.feelfreelinux.wykopmobilny.api.embed

import io.github.feelfreelinux.wykopmobilny.api.REMOVE_USERKEY_HEADER
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.WykopMobilnyUpdate
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Coub
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Gfycat
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.embed.Streamable
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

    @Headers("@: $REMOVE_USERKEY_HEADER")
    @GET("https://api.github.com/repos/feelfreelinux/WykopMobilny/releases/latest")
    fun checkUpdates(): Single<WykopMobilnyUpdate>

    @Headers("@: $REMOVE_USERKEY_HEADER")
    @GET("https://api.github.com/repos/feelfreelinux/WykopMobilnyWeekly/releases/latest")
    fun checkWeeklyUpdates(): Single<WykopMobilnyUpdate>
}
