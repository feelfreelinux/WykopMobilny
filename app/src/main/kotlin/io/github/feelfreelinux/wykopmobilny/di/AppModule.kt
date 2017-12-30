package io.github.feelfreelinux.wykopmobilny.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import javax.inject.Singleton

@Module
abstract class AppModule {
    @Binds
    abstract fun provideContext(application: WykopApp) : Context
}