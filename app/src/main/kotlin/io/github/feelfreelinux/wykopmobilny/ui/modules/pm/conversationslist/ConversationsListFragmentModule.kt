package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class ConversationsListFragmentModule {
    @Provides
    fun provideConversationsListPresenter(schedulers: Schedulers, pmApi: PMApi) =
        ConversationsListPresenter(schedulers, pmApi)
}
