package io.github.feelfreelinux.wykopmobilny.api.user

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LoginResponse
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import retrofit2.Retrofit

class LoginRepository(retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : LoginApi {
    private val tagApi by lazy { retrofit.create(LoginRetrofitApi::class.java) }

    override fun getUserSessionToken() =
        tagApi.getUserSessionToken(apiPreferences.login!!, apiPreferences.userKey!!)
                .compose<LoginResponse>(ErrorHandlerTransformer())
}