package io.github.feelfreelinux.wykopmobilny.api.user

import io.github.feelfreelinux.wykopmobilny.api.Profile
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import retrofit2.Call
import retrofit2.Retrofit

interface UserApi {
    fun getUserSessionToken() : Call<Profile>
}

class UserRepository(retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : UserApi {
    private val tagApi by lazy { retrofit.create(UserRetrofitApi::class.java) }

    override fun getUserSessionToken(): Call<Profile> {
        return tagApi.getUserSessionToken(apiPreferences.login!!, apiPreferences.userKey!!) }
}