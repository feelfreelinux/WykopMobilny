package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.WykopApiResponse
import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.api.responses.ObservedTagResponse
import io.github.wykopmobilny.api.responses.TagEntriesResponse
import io.github.wykopmobilny.api.responses.TagLinksResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TagRetrofitApi {
    @GET("/tags/entries/{tag}/appkey/$APP_KEY/page/{page}")
    fun getTagEntries(@Path("tag") tag: String, @Path("page") page: Int): Single<TagEntriesResponse>

    @GET("/tags/links/{tag}/appkey/$APP_KEY/page/{page}")
    fun getTagLinks(@Path("tag") tag: String, @Path("page") page: Int): Single<TagLinksResponse>

    @GET("/tags/observe/{tag}/appkey/$APP_KEY")
    fun observe(@Path("tag") tag: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/tags/unobserve/{tag}/appkey/$APP_KEY")
    fun unobserve(@Path("tag") tag: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/tags/block/{tag}/appkey/$APP_KEY")
    fun block(@Path("tag") tag: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/tags/unblock/{tag}/appkey/$APP_KEY")
    fun unblock(@Path("tag") tag: String): Single<WykopApiResponse<ObserveStateResponse>>

    @GET("/tags/observed/appkey/$APP_KEY")
    fun getObservedTags(): Single<WykopApiResponse<List<ObservedTagResponse>>>
}
