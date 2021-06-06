package io.github.wykopmobilny.di

import android.content.Context
import dagger.Binds
import dagger.Module
import io.github.wykopmobilny.TestApp

@Module
abstract class TestAppModule {

    @Binds
    abstract fun provideContext(application: TestApp): Context
}
