package io.github.wykopmobilny.ui.modules.mywykop.observedtags

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.tag.TagApi
import io.github.wykopmobilny.base.Schedulers

@Module
class MyWykopObservedTagsModule {
    @Provides
    fun provideMyWykopObservedTagsView(schedulers: Schedulers, tagApi: TagApi) =
        MyWykopObservedTagsPresenter(schedulers, tagApi)
}
