package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.BooleanResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.TagEntriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TagRetrofitApi {
    @GET("/tag/entries/{tag}/appkey/$APP_KEY/page/{page}")
    fun getTagEntries(@Path("tag") tag : String, @Path("page") page : Int) : Single<TagEntriesResponse>

    @GET("/tag/observe/{tag}/appkey/$APP_KEY")
    fun observe(@Path("tag") tag : String) : Single<BooleanResponse>

    @GET("/tag/unobserve/{tag}/appkey/$APP_KEY")
    fun unobserve(@Path("tag") tag : String) : Single<BooleanResponse>

    @GET("/tag/block/{tag}/appkey/$APP_KEY")
    fun block(@Path("tag") tag : String) : Single<BooleanResponse>

    @GET("/tag/unblock/{tag}/appkey/$APP_KEY")
    fun unblock(@Path("tag") tag : String) : Single<BooleanResponse>
}