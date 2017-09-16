package io.github.feelfreelinux.wykopmobilny.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.WykopApp
import javax.inject.Singleton

@Module
class AppModule(val app : WykopApp) {
    @Singleton
    @Provides
    fun provideContext() : Context = app

    @Singleton
    @Provides
    fun provideApplication() : Application = app
}