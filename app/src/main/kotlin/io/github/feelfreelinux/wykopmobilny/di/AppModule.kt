package io.github.feelfreelinux.wykopmobilny.di

import android.content.Context
import dagger.Binds
import dagger.Module
import io.github.feelfreelinux.wykopmobilny.WykopApp

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: WykopApp): Context
}
