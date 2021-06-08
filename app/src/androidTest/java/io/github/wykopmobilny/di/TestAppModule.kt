package io.github.wykopmobilny.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.tests.LoginScreenTest
import io.github.wykopmobilny.tests.MainScreenTest

@Module
abstract class TestAppModule {

    @Binds
    abstract fun provideContext(application: TestApp): Context

    @ContributesAndroidInjector
    abstract fun loginScreen(): LoginScreenTest

    @ContributesAndroidInjector
    abstract fun mainScreen(): MainScreenTest
}
