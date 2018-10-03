package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import kotlinx.android.synthetic.main.activity_mywykop.*

class MyWykopFragment : BaseFragment() {

    companion object {
        fun newInstance() = MyWykopFragment()
    }

    lateinit var pagerAdapter: MyWykopPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.activity_mywykop, container, false)
    }

    fun onRefresh() {
        for (i in 0 until pagerAdapter.registeredFragments.size()) {
            (pagerAdapter.registeredFragments.valueAt(i) as? SwipeRefreshLayout.OnRefreshListener)?.onRefresh()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
}