package io.github.wykopmobilny.ui.modules.pm.conversationslist

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.pm.PMApi
import io.github.wykopmobilny.base.Schedulers

@Module
class ConversationsListFragmentModule {
    @Provides
    fun provideConversationsListPresenter(schedulers: Schedulers, pmApi: PMApi) =
        ConversationsListPresenter(schedulers, pmApi)
}
