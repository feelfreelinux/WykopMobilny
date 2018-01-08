package io.github.feelfreelinux.wykopmobilny.ui.modules.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFragmentProvider {
    @ContributesAndroidInjector()
    abstract fun provideMainSettingsFragment(): SettingsFragment
}