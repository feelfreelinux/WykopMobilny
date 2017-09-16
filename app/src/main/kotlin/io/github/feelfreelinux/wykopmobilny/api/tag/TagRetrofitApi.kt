package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.api.TagFeedEntries
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TagRetrofitApi {
    @GET("/tag/entries/{tag}/appkey/$APP_KEY/page/{page}/{userkey}")
    fun getTagEntries(@Path("tag") tag : String, @Path("page") page : Int, @Path("userkey", encoded = true) userkey : String) : Call<TagFeedEntries>
}