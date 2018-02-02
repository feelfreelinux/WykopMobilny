package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added.AddedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added.AddedLinksModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.burried.BurriedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.burried.BurriedLinksModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments.ProfileLinkCommentsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments.ProfileLinksFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged.DiggedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged.DiggedLinksModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.published.PublishedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.published.PublishedLinksModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related.ProfileRelatedFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related.ProfileRelatedModule

@Module
abstract class LinksFragmentProvider {
    @ContributesAndroidInjector(modules = [AddedLinksModule::class])
    abstract fun provideAddedFragment(): AddedLinksFragment

    @ContributesAndroidInjector(modules = [ProfileLinksFragmentModule::class])
    abstract fun provideLinkComments(): ProfileLinkCommentsFragment

    @ContributesAndroidInjector(modules = [BurriedLinksModule::class])
    abstract fun provideBurriedLinksFragment(): BurriedLinksFragment

    @ContributesAndroidInjector(modules = [DiggedLinksModule::class])
    abstract fun provideDiggedLinksFragment(): DiggedLinksFragment

    @ContributesAndroidInjector(modules = [PublishedLinksModule::class])
    abstract fun providePublishedLinksFragment(): PublishedLinksFragment

    @ContributesAndroidInjector(modules = [ProfileRelatedModule::class])
    abstract fun provideRelatedFragment(): ProfileRelatedFragment
}