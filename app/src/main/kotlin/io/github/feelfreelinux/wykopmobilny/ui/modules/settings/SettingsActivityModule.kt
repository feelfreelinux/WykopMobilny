package io.github.feelfreelinux.wykopmobilny.ui.modules.settings

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi

@Module
class SettingsActivityModule {
    @Provides
    fun provideNavigator(settingsActivity: SettingsActivity): NewNavigatorApi = NewNavigator(settingsActivity)
}
