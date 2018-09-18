package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopEntryLinkFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopEntryLinkFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags.MyWykopObservedTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags.MyWykopObservedTagsModule

@Module
abstract class MyWykopFragmentProvider {
    @ContributesAndroidInjector(modules = [MyWykopEntryLinkFragmentModule::class])
    abstract fun provideIndexFragment(): MyWykopEntryLinkFragment

    @ContributesAndroidInjector(modules = [MyWykopObservedTagsModule::class])
    abstract fun provideObservedTagsFragment(): MyWykopObservedTagsFragment
}