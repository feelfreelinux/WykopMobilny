package io.github.wykopmobilny.ui.modules.input.link.edit

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers

@Module
class LinkCommentEditModule {
    @Provides
    fun provideEditLinkCommentPresenter(schedulers: Schedulers, linksApi: LinksApi) =
        LinkCommentEditPresenter(schedulers, linksApi)
}
