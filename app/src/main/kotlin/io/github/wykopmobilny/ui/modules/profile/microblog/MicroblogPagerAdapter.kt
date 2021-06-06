package io.github.wykopmobilny.ui.modules.profile.microblog

import android.content.res.Resources
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.modules.profile.microblog.comments.MicroblogCommentsFragment
import io.github.wykopmobilny.ui.modules.profile.microblog.entries.MicroblogEntriesFragment

class MicroblogPagerAdapter(
    private val resources: Resources,
    fragmentManager: FragmentManager,
) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {

    val registeredFragments = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment =
        if (position == 0) {
            MicroblogEntriesFragment.newInstance()
        } else {
            MicroblogCommentsFragment.newInstance()
        }

    override fun getCount() = 2

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
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
