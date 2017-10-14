package io.github.feelfreelinux.wykopmobilny.api.stream

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.EntryResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface StreamRetrofitApi {
    @GET("/stream/index/appkey/$APP_KEY/page/{page}")
    fun getMikroblogIndex(@Path("page") page : Int) : Single<List<EntryResponse>>

    @GET("/stream/hot/appkey/$APP_KEY/page/{page}/period/{period}")
    fun getMikroblogHot(@Path("page") page : Int, @Path("period") period: Int) : Single<List<EntryResponse>>
}