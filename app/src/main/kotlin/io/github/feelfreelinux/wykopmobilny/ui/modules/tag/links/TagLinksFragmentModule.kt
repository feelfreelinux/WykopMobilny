package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.links

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class TagLinksFragmentModule {
    @Provides
    fun provideTagLinksPresenter(schedulers: Schedulers, tagApi: TagApi, linksInteractor: LinksInteractor) =
            TagLinksPresenter(schedulers, tagApi, linksInteractor)
}