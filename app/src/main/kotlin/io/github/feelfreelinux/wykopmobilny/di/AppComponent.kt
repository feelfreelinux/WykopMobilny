package io.github.feelfreelinux.wykopmobilny.di

import android.app.Application
import android.content.Context
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import dagger.android.AndroidInjector
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.di.modules.NetworkModule
import io.github.feelfreelinux.wykopmobilny.di.modules.RepositoryModule
import dagger.BindsInstance



@Singleton
@Component( modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBuilder::class, NetworkModule::class, RepositoryModule::class])
internal interface AppComponent : AndroidInjector<WykopApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<WykopApp>()
}