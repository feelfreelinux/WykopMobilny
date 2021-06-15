package io.github.wykopmobilny.domain

import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import io.github.wykopmobilny.api.WykopApi
import io.github.wykopmobilny.blacklist.api.Scraper
import io.github.wykopmobilny.domain.login.ConnectConfig
import io.github.wykopmobilny.domain.login.LoginModule
import io.github.wykopmobilny.domain.login.LoginScope
import io.github.wykopmobilny.domain.navigation.Framework
import io.github.wykopmobilny.domain.promoted.PromotedModule
import io.github.wykopmobilny.storage.api.Storages
import io.github.wykopmobilny.ui.login.LoginDependencies
import javax.inject.Singleton

@Singleton
@Component(
    modules = [PromotedModule::class],
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
}

@LoginScope
@Subcomponent(modules = [LoginModule::class])
interface LoginDomainComponent : LoginDependencies

