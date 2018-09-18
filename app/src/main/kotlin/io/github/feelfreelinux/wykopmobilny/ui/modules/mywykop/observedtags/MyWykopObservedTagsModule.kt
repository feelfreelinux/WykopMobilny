package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class MyWykopObservedTagsModule {
    @Provides
    fun provideMyWykopObservedTagsView(schedulers: Schedulers, tagApi: TagApi) =
        MyWykopObservedTagsPresenter(schedulers, tagApi)
}