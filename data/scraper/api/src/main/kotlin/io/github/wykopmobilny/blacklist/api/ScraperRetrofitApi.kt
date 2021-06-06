package io.github.wykopmobilny.blacklist.api

import io.reactivex.Single
import retrofit2.http.GET

interface ScraperRetrofitApi {

    @GET("ustawienia/czarne-listy/")
    fun getBlacklist(): Single<Blacklist>
}
