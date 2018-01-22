package io.github.feelfreelinux.wykopmobilny.ui.modules.tag

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries.TagEntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries.TagEntriesModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.links.TagLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.links.TagLinksFragmentModule

@Module
abstract class TagActivityFragmentProvider {
    @ContributesAndroidInjector(modules = [TagEntriesModule::class])
    abstract fun provideEntryFragment() : TagEntriesFragment

    @ContributesAndroidInjector(modules = [TagLinksFragmentModule::class])
    abstract fun provideLinksFragment() : TagLinksFragment
}