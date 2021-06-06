package io.github.wykopmobilny.di

import android.content.Context
import dagger.Binds
import dagger.Module
import io.github.wykopmobilny.WykopApp

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: WykopApp): Context
}
