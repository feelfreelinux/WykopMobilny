package io.github.feelfreelinux.wykopmobilny.api.tag

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.TagEntriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TagRetrofitApi {
    @GET("/tag/entries/{tag}/appkey/$APP_KEY/page/{page}")
    fun getTagEntries(@Path("tag") tag : String, @Path("page") page : Int) : Single<TagEntriesResponse>
}