package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class HotFragmentModule {
    @Provides
    fun providesHotPresenter(schedulers: Schedulers, streamApi: EntriesApi) =
        HotPresenter(schedulers, streamApi)
}
