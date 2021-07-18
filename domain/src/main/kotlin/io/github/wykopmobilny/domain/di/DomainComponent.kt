package io.github.wykopmobilny.domain.di

import dagger.BindsInstance
import dagger.Component
import io.github.wykopmobilny.api.WykopApi
import io.github.wykopmobilny.blacklist.api.Scraper
import io.github.wykopmobilny.domain.login.ConnectConfig
import io.github.wykopmobilny.domain.login.di.LoginDomainComponent
import io.github.wykopmobilny.domain.navigation.Framework
import io.github.wykopmobilny.domain.navigation.InteropModule
import io.github.wykopmobilny.domain.navigation.InteropRequestService
import io.github.wykopmobilny.domain.promoted.PromotedModule
import io.github.wykopmobilny.domain.settings.di.SettingsDomainComponent
import io.github.wykopmobilny.domain.styles.di.StylesDomainComponent
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.storage.api.Storages
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        InteropModule::class,
        StoresModule::class,
        PromotedModule::class,
    ],
    dependencies = [
        Storages::class,
        Scraper::class,
        WykopApi::class,
        Framework::class,
    ],
)
interface DomainComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance connectConfig: ConnectConfig,
            storages: Storages,
            scraper: Scraper,
            wykop: WykopApi,
            framework: Framework,
        ): DomainComponent
    }

    fun login(): LoginDomainComponent

    fun styles(): StylesDomainComponent

    fun settings(): SettingsDomainComponent

    fun navigation(): InteropRequestService

    fun settingsApiInterop(): SettingsPreferencesApi
}
