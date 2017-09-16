package io.github.feelfreelinux.wykopmobilny.api.stream

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.api.Entry
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StreamRetrofitApi {
    @GET("/stream/index/appkey/$APP_KEY/page/{page}/{userkey}")
    fun getMikroblogIndex(@Path("page") page : Int, @Path("userkey", encoded = true) userkey : String) : Call<List<Entry>>

    @GET("/stream/hot/appkey/$APP_KEY/page/{page}/period/{period}/{userkey}")
    fun getMikroblogHot(@Path("page") page : Int, @Path("period") period: Int, @Path("userkey", encoded = true) userkey : String) : Call<List<Entry>>
}