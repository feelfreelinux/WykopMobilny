package io.github.wykopmobilny.domain.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.endpoints.LoginRetrofitApi
import io.github.wykopmobilny.blacklist.api.ScraperRetrofitApi
import io.github.wykopmobilny.domain.api.apiCall
import io.github.wykopmobilny.storage.api.Blacklist
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.github.wykopmobilny.storage.api.LoggedUserInfo
import io.github.wykopmobilny.storage.api.UserInfoStorage
import io.github.wykopmobilny.storage.api.UserSession
import javax.inject.Singleton

@Module
internal class StoresModule {

    @Singleton
    @Provides
    fun blacklistStore(
        retrofitApi: ScraperRetrofitApi,
        storage: BlacklistPreferencesApi,
    ) = StoreBuilder.from<Unit, Blacklist, Blacklist>(
        fetcher = Fetcher.of {
            val api = retrofitApi.getBlacklist()
            Blacklist(
                tags = api.tags?.tags?.mapNotNull { it.tag?.removePrefix("#") }?.toSet().orEmpty(),
                users = api.users?.users?.mapNotNull { it.nick?.removePrefix("@") }?.toSet().orEmpty(),
            )
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { storage.blacklist },
            writer = { _, newValue -> storage.update { newValue } },
            delete = { storage.clear() },
            deleteAll = { storage.clear() },
        ),
    )
        .build()

    @Singleton
    @Provides
    fun loginStore(
        retrofitApi: LoginRetrofitApi,
        storage: UserInfoStorage,
    ) = StoreBuilder.from(
        fetcher = Fetcher.ofResult { request: UserSession ->
            apiCall(
                rawCall = { retrofitApi.getUserSessionToken(request.login, request.token) },
                mapping = {
                    LoggedUserInfo(
                        userName = profile.login,
                        userToken = userkey,
                        avatarUrl = profile.avatar,
                        backgroundUrl = profile.background,
                    )
                },
            )
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { storage.loggedUser },
            writer = { _, newValue -> storage.updateLoggedUser(newValue) },
            delete = { storage.updateLoggedUser(null) },
            deleteAll = { storage.updateLoggedUser(null) },
        ),
    )
        .build()
}
