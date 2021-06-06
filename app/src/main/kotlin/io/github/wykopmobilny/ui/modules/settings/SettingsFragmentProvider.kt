package io.github.wykopmobilny.ui.modules.settings

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFragmentProvider {
    @ContributesAndroidInjector
    abstract fun provideMainSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun provideAppereanceFragment(): SettingsAppearance
}
