package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BlacklistPagerAdapter(fragmentManager : androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): androidx.fragment.app.Fragment {
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