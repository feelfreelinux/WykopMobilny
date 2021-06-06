package io.github.wykopmobilny.ui.modules.embedview

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.embed.ExternalApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class EmbedViewModule {
    @Provides
    fun providesEmbedViewPresenter(schedulers: Schedulers, embedApi: ExternalApi) =
        EmbedLinkPresenter(schedulers, embedApi)

    @Provides
    fun provideNavigator(activity: EmbedViewActivity): NewNavigatorApi =
        NewNavigator(activity)

    @Provides
    fun provideLinkHandler(
        activity: EmbedViewActivity,
        navigator: NewNavigatorApi,
        settingsPreferences: SettingsPreferencesApi,
    ): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator, settingsPreferences)
}
