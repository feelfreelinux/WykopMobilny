package io.github.wykopmobilny.storage.android

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.github.wykopmobilny.storage.api.LinksPreferencesApi
import io.github.wykopmobilny.storage.api.SessionStorage
import io.github.wykopmobilny.storage.api.Storages
import io.github.wykopmobilny.storage.api.UserInfoStorage
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import java.util.concurrent.Executor

@Component(modules = [StoragesModule::class])
interface StoragesComponent : Storages {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance executor: Executor,
        ): StoragesComponent
    }
}

@Module
internal abstract class StoragesModule {

    @Binds
    abstract fun LinksPreferences.provideLinksPreferencesApi(): LinksPreferencesApi

    @Binds
    abstract fun BlacklistPreferences.provideBlacklistApi(): BlacklistPreferencesApi

    @Binds
    abstract fun CredentialsPreferences.sessionStorage(): SessionStorage

    @Binds
    abstract fun CredentialsPreferences.userInfoStorage(): UserInfoStorage

    @Binds
    abstract fun UserPreferences.userPreferences(): UserPreferenceApi
}
