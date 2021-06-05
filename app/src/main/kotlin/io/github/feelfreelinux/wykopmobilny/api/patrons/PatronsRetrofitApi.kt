package io.github.feelfreelinux.wykopmobilny.api.patrons

import io.github.feelfreelinux.wykopmobilny.api.REMOVE_USERKEY_HEADER
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons.PatronsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface PatronsRetrofitApi {
    @Headers("@: $REMOVE_USERKEY_HEADER")
    @GET("https://raw.githubusercontent.com/alufers/owm-patrons/master/patrons.json")
    fun getPatrons(): Single<PatronsResponse>
}
