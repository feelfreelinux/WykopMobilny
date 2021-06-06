package io.github.wykopmobilny.ui.modules.profile.microblog

import android.content.res.Resources
import android.util.SparseArray
import android.view.ViewGroup
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.modules.profile.microblog.comments.MicroblogCommentsFragment
import io.github.wykopmobilny.ui.modules.profile.microblog.entries.MicroblogEntriesFragment

class MicroblogPagerAdapter(
    val resources: Resources,
    fragmentManager: androidx.fragment.app.FragmentManager
) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {

    val registeredFragments = SparseArray<androidx.fragment.app.Fragment>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment = when (position) {
        0 -> MicroblogEntriesFragment.newInstance()
        else -> MicroblogCommentsFragment.newInstance()
    }

    override fun getCount() = 2

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as androidx.fragment.app.Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.removeAt(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int) =
        if (position == 0) {
            R.string.entries
        } else {
            R.string.commented
        }
            .let(resources::getString)
}
