package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added.ProfileLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added.ProfileLinksModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments.ProfileLinkCommentsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments.ProfileLinksFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related.ProfileRelatedFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related.ProfileRelatedModule

@Module
abstract class LinksFragmentProvider {
    @ContributesAndroidInjector(modules = [ProfileLinksModule::class])
    abstract fun provideAddedFragment(): ProfileLinksFragment

    @ContributesAndroidInjector(modules = [ProfileLinksFragmentModule::class])
    abstract fun provideLinkComments(): ProfileLinkCommentsFragment

    @ContributesAndroidInjector(modules = [ProfileRelatedModule::class])
    abstract fun provideRelatedFragment(): ProfileRelatedFragment
}