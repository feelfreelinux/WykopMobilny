package io.github.feelfreelinux.wykopmobilny.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.di.modules.NetworkModule
import io.github.feelfreelinux.wykopmobilny.di.modules.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class,
        NetworkModule::class,
        RepositoryModule::class,
    ]
)

internal interface AppComponent : AndroidInjector<WykopApp> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<WykopApp>
}
