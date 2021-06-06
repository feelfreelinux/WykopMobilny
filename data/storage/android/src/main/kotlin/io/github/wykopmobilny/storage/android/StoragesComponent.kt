package io.github.wykopmobilny.storage.android

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import io.github.wykopmobilny.storage.api.BlacklistPreferencesApi
import io.github.wykopmobilny.storage.api.CredentialsPreferencesApi
import io.github.wykopmobilny.storage.api.LinksPreferencesApi
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.utils.api.CredentialsPreferences

@Component(modules = [StoragesModule::class])
interface StoragesComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): StoragesComponent
    }

    fun settingsPreferences(): SettingsPreferencesApi

    fun linksPreferences(): LinksPreferencesApi

    fun blacklistPreferences(): BlacklistPreferencesApi

    fun credentialsPreferences(): CredentialsPreferencesApi
}

@Module
internal abstract class StoragesModule {

    @Binds
    abstract fun SettingsPreferences.provideSettingsPreferences(): SettingsPreferencesApi

    @Binds
    abstract fun LinksPreferences.provideLinksPreferencesApi(): LinksPreferencesApi

    @Binds
    abstract fun BlacklistPreferences.provideBlacklistApi(): BlacklistPreferencesApi

    @Binds
    abstract fun CredentialsPreferences.credentialsPreferencesApi(): CredentialsPreferencesApi
}
