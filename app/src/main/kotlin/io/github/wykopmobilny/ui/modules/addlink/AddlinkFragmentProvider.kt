package io.github.wykopmobilny.ui.modules.addlink

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.github.wykopmobilny.ui.modules.addlink.fragments.confirmdetails.AddLinkDetailsFragment
import io.github.wykopmobilny.ui.modules.addlink.fragments.confirmdetails.AddLinkDetailsFragmentModule
import io.github.wykopmobilny.ui.modules.addlink.fragments.duplicateslist.AddLinkDuplicatesListFragment
import io.github.wykopmobilny.ui.modules.addlink.fragments.urlinput.AddLinkUrlInputModule
import io.github.wykopmobilny.ui.modules.addlink.fragments.urlinput.AddlinkUrlInputFragment

@Module
abstract class AddlinkFragmentProvider {
    @ContributesAndroidInjector(modules = [AddLinkUrlInputModule::class])
    abstract fun bindAddLinkUrlInputFragment(): AddlinkUrlInputFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun bindAddLinkDuplicatesFragment(): AddLinkDuplicatesListFragment

    @ContributesAndroidInjector(modules = [AddLinkDetailsFragmentModule::class])
    abstract fun bindAddLinkDetailsFragment(): AddLinkDetailsFragment
}
