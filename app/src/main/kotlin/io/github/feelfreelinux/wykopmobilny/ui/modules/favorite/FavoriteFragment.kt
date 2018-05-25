package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopNotifier
import kotlinx.android.synthetic.main.activity_mywykop.*

class FavoriteFragment  : BaseFragment() {
    lateinit var pagerAdapter : FavoritePagerAdapter

    companion object {
        fun newInstance(): Fragment {
            return FavoriteFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.activity_mywykop, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = FavoritePagerAdapter(resources, childFragmentManager)
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.favourite)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_layout, menu)
    }

    fun onRefresh() {
        for (i in 0 until pagerAdapter.registeredFragments.size()) {
            (pagerAdapter.registeredFragments.valueAt(i) as FavoriteFragmentNotifier)
                    .onRefresh()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.refresh -> onRefresh()
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving)
        for (i in 0 until pagerAdapter.registeredFragments.size()) {
            (pagerAdapter.registeredFragments.valueAt(i) as FavoriteFragmentNotifier)
                    .removeDataFragment()
        }
    }
}