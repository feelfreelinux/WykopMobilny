package io.github.wykopmobilny.domain.login.di

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.endpoints.LoginRetrofitApi
import io.github.wykopmobilny.domain.api.apiCall
import io.github.wykopmobilny.domain.login.LoginQuery
import io.github.wykopmobilny.domain.utils.InMemoryScopedViewState
import io.github.wykopmobilny.storage.api.LoggedUserInfo
import io.github.wykopmobilny.storage.api.UserInfoStorage
import io.github.wykopmobilny.storage.api.UserSession
import io.github.wykopmobilny.ui.base.ScopedViewState
import io.github.wykopmobilny.ui.login.Login

@Module
internal abstract class LoginModule {

    @Binds
    abstract fun LoginQuery.login(): Login

    @Binds
    @LoginScope
    abstract fun InMemoryScopedViewState.viewState(): ScopedViewState

    companion object {

        @LoginScope
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
}
