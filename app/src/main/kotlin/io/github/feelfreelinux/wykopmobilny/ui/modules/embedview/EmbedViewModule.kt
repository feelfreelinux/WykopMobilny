package io.github.feelfreelinux.wykopmobilny.ui.modules.embedview

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.embed.ExternalApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@Module
class EmbedViewModule {
    @Provides
    fun providesEmbedViewPresenter(schedulers: Schedulers, embedApi: ExternalApi) =
        EmbedLinkPresenter(schedulers, embedApi)

    @Provides
    fun provideNavigator(activity: EmbedViewActivity): NewNavigatorApi =
        NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: EmbedViewActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigator)
}
