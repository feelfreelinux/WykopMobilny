package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added.AddedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.burried.BurriedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments.ProfileLinkCommentsFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged.DiggedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.published.PublishedLinksFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related.ProfileRelatedFragment

class LinksPagerAdapter(val resources : Resources, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    val registeredFragments = SparseArray<Fragment>()

    override fun getItem(position: Int): Fragment = when(position) {
        0 -> AddedLinksFragment.newInstance()
        1 -> PublishedLinksFragment.newInstance()
        2 -> ProfileLinkCommentsFragment.newInstance()
        3 -> ProfileRelatedFragment.newInstance()
        4 -> DiggedLinksFragment.newInstance()
        else -> BurriedLinksFragment.newInstance()
    }

    override fun getCount() = 6

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.removeAt(position)
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence {
        super.getPageTitle(position)
        return when(position) {
            0 -> resources.getString(R.string.link_added)
            1 -> resources.getString(R.string.published)
            2 -> resources.getString(R.string.commented)
            3 -> resources.getString(R.string.related)
            4 -> resources.getString(R.string.digged)
            else -> resources.getString(R.string.buried)
        }
    }
}