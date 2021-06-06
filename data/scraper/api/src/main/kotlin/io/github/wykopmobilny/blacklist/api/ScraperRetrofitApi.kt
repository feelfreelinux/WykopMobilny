package io.github.wykopmobilny.blacklist.api

import retrofit2.http.GET

interface ScraperRetrofitApi {

    @GET("ustawienia/czarne-listy/")
    suspend fun getBlacklist(): Blacklist
}
