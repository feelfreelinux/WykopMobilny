package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags.MyWykopObservedTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragment

class MyWykopPagerAdapter(val resources : Resources, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = when(position) {
        0 -> MyWykopIndexFragment.newInstance()
        1 -> MyWykopTagsFragment.newInstance()
        2 -> MyWykopUsersFragment.newInstance()
        else -> MyWykopObservedTagsFragment.newInstance()
    }

    override fun getCount() = 4

    override fun getPageTitle(position: Int): CharSequence {
        super.getPageTitle(position)
        return when(position) {
            0 -> resources.getString(R.string.mywykop_all)
            1 -> resources.getString(R.string.tags)
            2 -> resources.getString(R.string.users)
            else -> resources.getString(R.string.observed_tags)
        }
    }
}