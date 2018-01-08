package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragmentModule
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragmentModule

@Module
abstract class MyWykopFragmentProvider {
    @ContributesAndroidInjector(modules = [MyWykopIndexFragmentModule::class])
    abstract fun provideIndexFragment() : MyWykopIndexFragment

    @ContributesAndroidInjector(modules = [MyWykopTagsFragmentModule::class])
    abstract fun provideTagsFragment() : MyWykopTagsFragment

    @ContributesAndroidInjector(modules = [MyWykopUsersFragmentModule::class])
    abstract fun provideUsersFragment() : MyWykopUsersFragment
}