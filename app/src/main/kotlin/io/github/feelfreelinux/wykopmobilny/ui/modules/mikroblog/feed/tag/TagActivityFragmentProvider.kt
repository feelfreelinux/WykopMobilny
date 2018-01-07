package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries.TagEntriesFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries.TagEntriesModule

@Module
abstract class TagActivityFragmentProvider {
    @ContributesAndroidInjector(modules = [TagEntriesModule::class])
    abstract fun provideEntryFragment() : TagEntriesFragment
}