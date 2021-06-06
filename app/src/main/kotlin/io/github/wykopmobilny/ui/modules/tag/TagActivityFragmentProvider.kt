package io.github.wykopmobilny.ui.modules.tag

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.wykopmobilny.ui.modules.tag.entries.TagEntriesFragment
import io.github.wykopmobilny.ui.modules.tag.entries.TagEntriesModule
import io.github.wykopmobilny.ui.modules.tag.links.TagLinksFragment
import io.github.wykopmobilny.ui.modules.tag.links.TagLinksFragmentModule

@Module
abstract class TagActivityFragmentProvider {
    @ContributesAndroidInjector(modules = [TagEntriesModule::class])
    abstract fun provideEntryFragment(): TagEntriesFragment

    @ContributesAndroidInjector(modules = [TagLinksFragmentModule::class])
    abstract fun provideLinksFragment(): TagLinksFragment
}
