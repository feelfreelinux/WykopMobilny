package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class BlacklistPagerAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> BlacklistTagsFragment.createFragment()
            else -> BlacklistUsersFragment.createFragment()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Tagi"
            else -> "Użytkownicy"
        }
    }
}