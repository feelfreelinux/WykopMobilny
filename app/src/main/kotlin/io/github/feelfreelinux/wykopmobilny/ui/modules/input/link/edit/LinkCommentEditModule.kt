package io.github.feelfreelinux.wykopmobilny.ui.modules.input.link.edit

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class LinkCommentEditModule {
    @Provides
    fun provideEditLinkCommentPresenter(schedulers: Schedulers, linksApi: LinksApi) =
        LinkCommentEditPresenter(schedulers, linksApi)
}