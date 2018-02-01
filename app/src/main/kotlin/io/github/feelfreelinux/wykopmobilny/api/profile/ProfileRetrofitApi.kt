package io.github.feelfreelinux.wykopmobilny.api.profile

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileRetrofitApi {
    @GET("/profiles/index/{username}/appkey/$APP_KEY")
    fun getIndex(@Path("username") username : String) : Single<WykopApiResponse<ProfileResponse>>
}