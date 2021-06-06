package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.REMOVE_USERKEY_HEADER
import io.github.wykopmobilny.api.responses.WykopApiResponse
import io.github.wykopmobilny.api.responses.LoginResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginRetrofitApi {
    @Headers("@: $REMOVE_USERKEY_HEADER")
    @FormUrlEncoded
    @POST("/login/index/appkey/$APP_KEY")
    fun getUserSessionToken(
        @Field("login") login: String,
        @Field("accountkey", encoded = true) accountKey: String
    ): Single<WykopApiResponse<LoginResponse>>
}
