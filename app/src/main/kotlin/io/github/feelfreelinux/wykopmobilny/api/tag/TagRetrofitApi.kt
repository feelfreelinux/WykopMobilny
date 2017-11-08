package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagEntriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TagRetrofitApi {
    @GET("/tags/entries/{tag}/appkey/$APP_KEY/page/{page}")
    fun getTagEntries(@Path("tag") tag : String, @Path("page") page : Int) : Single<TagEntriesResponse>

    @GET("/tags/observe/{tag}/appkey/$APP_KEY")
    fun observe(@Path("tag") tag : String) : Single<WykopApiResponse<TagStateResponse>>

    @GET("/tags/unobserve/{tag}/appkey/$APP_KEY")
    fun unobserve(@Path("tag") tag : String) : Single<WykopApiResponse<TagStateResponse>>

    @GET("/tags/block/{tag}/appkey/$APP_KEY")
    fun block(@Path("tag") tag : String) : Single<WykopApiResponse<TagStateResponse>>

    @GET("/tags/unblock/{tag}/appkey/$APP_KEY")
    fun unblock(@Path("tag") tag : String) : Single<WykopApiResponse<TagStateResponse>>
}