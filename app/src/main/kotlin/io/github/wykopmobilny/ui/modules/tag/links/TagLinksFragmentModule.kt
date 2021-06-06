package io.github.wykopmobilny.ui.modules.tag.links

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.tag.TagApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class TagLinksFragmentModule {
    @Provides
    fun provideTagLinksPresenter(schedulers: Schedulers, tagApi: TagApi, linksInteractor: LinksInteractor) =
        TagLinksPresenter(schedulers, tagApi, linksInteractor)
}
