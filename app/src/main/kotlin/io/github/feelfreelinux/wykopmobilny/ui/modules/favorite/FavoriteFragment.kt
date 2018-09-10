package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite

import android.os.Bundle
import android.view.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import kotlinx.android.synthetic.main.activity_mywykop.*

class FavoriteFragment  : BaseFragment() {
    lateinit var pagerAdapter : FavoritePagerAdapter

    companion object {
        fun newInstance(): androidx.fragment.app.Fragment {
            return FavoriteFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.activity_mywykop, container, false)
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
            (pagerAdapter.registeredFragments.valueAt(i) as? SwipeRefreshLayout.OnRefreshListener)?.onRefresh()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.refresh -> onRefresh()
        }
        return true
    }
}