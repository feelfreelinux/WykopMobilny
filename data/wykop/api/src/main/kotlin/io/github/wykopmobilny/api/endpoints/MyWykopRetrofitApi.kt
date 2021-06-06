package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.EntryLinkResponse
import io.github.wykopmobilny.api.responses.WykopApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MyWykopRetrofitApi {

    @GET("/mywykop/index/page/{page}/appkey/$APP_KEY")
    suspend fun getIndex(@Path("page") page: Int): WykopApiResponse<List<EntryLinkResponse>>

    @GET("/mywykop/users/page/{page}/appkey/$APP_KEY")
    suspend fun byUsers(@Path("page") page: Int): WykopApiResponse<List<EntryLinkResponse>>

    @GET("/mywykop/tags/page/{page}/appkey/$APP_KEY")
    suspend fun byTags(@Path("page") page: Int): WykopApiResponse<List<EntryLinkResponse>>
}
