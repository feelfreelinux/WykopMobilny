package io.github.wykopmobilny.patrons.api

import io.reactivex.Single
import retrofit2.http.GET

interface PatronsRetrofitApi {

    @GET("alufers/owm-patrons/master/patrons.json")
    fun getPatrons(): Single<PatronsResponse>
}
