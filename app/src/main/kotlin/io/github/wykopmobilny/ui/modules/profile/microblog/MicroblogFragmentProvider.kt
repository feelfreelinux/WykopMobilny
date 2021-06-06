package io.github.wykopmobilny.ui.modules.profile.microblog

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.wykopmobilny.ui.modules.profile.microblog.comments.MicroblogCommentsFragment
import io.github.wykopmobilny.ui.modules.profile.microblog.comments.MicroblogCommentsModule
import io.github.wykopmobilny.ui.modules.profile.microblog.entries.MicroblogEntriesFragment
import io.github.wykopmobilny.ui.modules.profile.microblog.entries.MicroblogEntriesModule

@Module
abstract class MicroblogFragmentProvider {
    @ContributesAndroidInjector(modules = [MicroblogEntriesModule::class])
    abstract fun provideEntriesFragment(): MicroblogEntriesFragment

    @ContributesAndroidInjector(modules = [MicroblogCommentsModule::class])
    abstract fun provideCommentsFragment(): MicroblogCommentsFragment
}
