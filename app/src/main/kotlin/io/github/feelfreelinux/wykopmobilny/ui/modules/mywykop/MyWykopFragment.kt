package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.FavoriteFragmentNotifier
import kotlinx.android.synthetic.main.activity_mywykop.*

class MyWykopFragment : BaseFragment() {
    lateinit var pagerAdapter : MyWykopPagerAdapter

    companion object {
        fun newInstance(): Fragment {
            return MyWykopFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.activity_mywykop, container, false)
        return view
    }

    fun onRefresh() {
        for (i in 0 until pagerAdapter.registeredFragments.size()) {
            (pagerAdapter.registeredFragments.valueAt(i) as MyWykopNotifier)
                    .onRefresh()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.refresh -> onRefresh()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_layout, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = MyWykopPagerAdapter(resources, childFragmentManager)
        pager.offscreenPageLimit = 4
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.mywykop)

    }

    override fun onPause() {
        super.onPause()
        if (isRemoving)
            for (i in 0 until pagerAdapter.registeredFragments.size()) {
                (pagerAdapter.registeredFragments.valueAt(i) as MyWykopNotifier)
                        .removeDataFragment()
            }
    }
}