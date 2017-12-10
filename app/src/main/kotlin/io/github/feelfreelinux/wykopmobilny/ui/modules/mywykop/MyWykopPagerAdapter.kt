package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index.MyWykopIndexFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users.MyWykopUsersFragment
import io.github.feelfreelinux.wykopmobilny.utils.printout

class MyWykopPagerAdapter(val resources : Resources, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = when(position) {
        0 -> MyWykopIndexFragment.newInstance()
        1 -> MyWykopTagsFragment.newInstance()
        else -> MyWykopUsersFragment.newInstance()
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int): CharSequence {
        super.getPageTitle(position)
        return when(position) {
            0 -> resources.getString(R.string.all)
            1 -> resources.getString(R.string.tags)
            else -> resources.getString(R.string.users)
        }
    }
}