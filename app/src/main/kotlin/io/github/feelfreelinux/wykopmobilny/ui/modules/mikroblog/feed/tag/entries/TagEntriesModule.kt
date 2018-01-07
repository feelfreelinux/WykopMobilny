package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class TagEntriesModule {
    @Provides
    fun provideTagEntriesPresenter(schedulers: Schedulers, tagApi: TagApi) =
            TagEntriesPresenter(schedulers, tagApi)
}