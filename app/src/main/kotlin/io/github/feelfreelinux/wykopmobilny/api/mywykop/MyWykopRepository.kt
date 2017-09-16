package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.api.NotificationCountResponse
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.userSessionToken
import retrofit2.Call
import retrofit2.Retrofit

interface MyWykopApi {
    fun getNotificationCount(): Call<NotificationCountResponse>
    fun getHashTagNotificationCount(): Call<NotificationCountResponse>
}

class MyWykopRepository(val retrofit: Retrofit, private val apiPreferences: CredentialsPreferencesApi) : MyWykopApi {
    private val mywykopApi by lazy { retrofit.create(MyWykopRetrofitApi::class.java) }

    override fun getNotificationCount() = mywykopApi.getNotificationCount(apiPreferences.userSessionToken)

    override fun getHashTagNotificationCount() = mywykopApi.getHashTagsNotificationsCount(apiPreferences.userSessionToken)

}