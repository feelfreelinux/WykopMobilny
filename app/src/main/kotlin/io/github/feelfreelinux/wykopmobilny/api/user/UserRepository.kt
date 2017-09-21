package io.github.feelfreelinux.wykopmobilny.api.user

import io.github.feelfreelinux.wykopmobilny.models.pojo.Profile
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.reactivex.Single
import retrofit2.Retrofit

interface UserApi {
    fun getUserSessionToken() : Single<Profile>
}

class UserRepository(retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : UserApi {
    private val tagApi by lazy { retrofit.create(UserRetrofitApi::class.java) }

    override fun getUserSessionToken() =
        tagApi.getUserSessionToken(apiPreferences.login!!, apiPreferences.userKey!!)
}