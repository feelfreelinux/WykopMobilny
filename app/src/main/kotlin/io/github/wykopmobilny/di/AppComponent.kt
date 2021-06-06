package io.github.wykopmobilny.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.github.wykopmobilny.WykopApp
import io.github.wykopmobilny.blacklist.remote.ScraperComponent
import io.github.wykopmobilny.di.modules.NetworkModule
import io.github.wykopmobilny.di.modules.RepositoryModule
import io.github.wykopmobilny.patrons.remote.PatronsComponent
import io.github.wykopmobilny.wykop.remote.WykopComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class,
        NetworkModule::class,
        RepositoryModule::class,
    ],
    dependencies = [
        WykopComponent::class,
        PatronsComponent::class,
        ScraperComponent::class,
    ],
)

internal interface AppComponent : AndroidInjector<WykopApp> {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance instance: WykopApp,
            @BindsInstance okHttpClient: OkHttpClient,
            wykop: WykopComponent,
            patrons: PatronsComponent,
            scraper: ScraperComponent,
        ): AppComponent
    }
}
