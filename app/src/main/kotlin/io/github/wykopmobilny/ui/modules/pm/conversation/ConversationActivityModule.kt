package io.github.wykopmobilny.ui.modules.pm.conversation

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.pm.PMApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class ConversationActivityModule {
    @Provides
    fun provideConversationActivityPresenter(schedulers: Schedulers, pmApi: PMApi) = ConversationPresenter(schedulers, pmApi)

    @Provides
    fun provideNavigatorApi(activity: ConversationActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandlerApi(navigatorApi: NewNavigatorApi, activity: ConversationActivity): WykopLinkHandlerApi =
        WykopLinkHandler(activity, navigatorApi)
}
