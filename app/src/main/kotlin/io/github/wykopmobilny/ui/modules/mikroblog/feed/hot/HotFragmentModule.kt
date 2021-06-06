package io.github.wykopmobilny.ui.modules.mikroblog.feed.hot

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.base.Schedulers

@Module
class HotFragmentModule {
    @Provides
    fun providesHotPresenter(schedulers: Schedulers, streamApi: EntriesApi) =
        HotPresenter(schedulers, streamApi)
}
