package io.github.wykopmobilny.ui.settings.android.di

import dagger.Component
import io.github.wykopmobilny.ui.settings.SettingsDependencies
import io.github.wykopmobilny.ui.settings.android.AppearancePreferencesFragment
import io.github.wykopmobilny.ui.settings.android.GeneralPreferencesFragment
import io.github.wykopmobilny.ui.settings.android.MainPreferencesFragment

@Component(dependencies = [SettingsDependencies::class])
internal interface SettingsUiComponent {

    fun inject(fragment: MainPreferencesFragment)

    fun inject(fragment: AppearancePreferencesFragment)

    fun inject(fragment: GeneralPreferencesFragment)

    @Component.Factory
    interface Factory {

        fun create(
            deps: SettingsDependencies,
        ): SettingsUiComponent
    }
}
