package io.github.wykopmobilny.ui.modules.tag

import android.content.res.Resources
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.github.wykopmobilny.R
import io.github.wykopmobilny.ui.modules.tag.entries.TagEntriesFragment
import io.github.wykopmobilny.ui.modules.tag.links.TagLinksFragment

class TagPagerAdapter(
    private val tag: String,
    private val resources: Resources,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    val registeredFragments = SparseArray<androidx.fragment.app.Fragment>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return if (position == 0) {
            TagLinksFragment.newInstance(tag)
        } else {
            TagEntriesFragment.newInstance(tag)
        }
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
            R.string.links
        } else {
            R.string.entries
        }
            .let(resources::getString)
}
