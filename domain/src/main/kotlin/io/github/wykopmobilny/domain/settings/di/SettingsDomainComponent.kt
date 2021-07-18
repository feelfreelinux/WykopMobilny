package io.github.wykopmobilny.domain.settings.di

import dagger.Subcomponent
import io.github.wykopmobilny.ui.settings.SettingsDependencies

@SettingsScope
@Subcomponent(modules = [SettingsModule::class])
interface SettingsDomainComponent : SettingsDependencies
