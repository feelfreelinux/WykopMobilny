package io.github.wykopmobilny.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.blacklist.remote.ScraperComponent
import io.github.wykopmobilny.di.modules.NetworkModule
import io.github.wykopmobilny.di.modules.RepositoryModule
import io.github.wykopmobilny.fakes.FakeCookieProvider
import io.github.wykopmobilny.patrons.remote.PatronsComponent
import io.github.wykopmobilny.storage.android.StoragesComponent
import io.github.wykopmobilny.wykop.remote.WykopComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TestAppModule::class,
        ActivityBuilder::class,
        NetworkModule::class,
        RepositoryModule::class,
    ],
    dependencies = [
        WykopComponent::class,
        PatronsComponent::class,
        ScraperComponent::class,
        StoragesComponent::class,
    ],
)
interface TestAppComponent : AppComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance instance: TestApp,
            @BindsInstance okHttpClient: OkHttpClient,
            wykop: WykopComponent,
            patrons: PatronsComponent,
            scraper: ScraperComponent,
            storages: StoragesComponent,
        ): TestAppComponent
    }

    fun cookieProvider(): FakeCookieProvider
}
