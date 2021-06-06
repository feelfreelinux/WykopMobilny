package io.github.wykopmobilny.ui.modules.profile

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class ProfileModule {

    @Provides
    fun providePresenter(schedulers: Schedulers, profilesApi: ProfileApi) = ProfilePresenter(schedulers, profilesApi)

    @Provides
    fun provideNavigator(activity: ProfileActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(
        activity: ProfileActivity,
        navigator: NewNavigatorApi,
        settingsPreferences: SettingsPreferencesApi,
    ): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator, settingsPreferences)
}
