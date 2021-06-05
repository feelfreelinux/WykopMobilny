package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.utils.hideKeyboard
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*

class SearchFragment : BaseFragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    val querySubject by lazy { PublishSubject.create<String>() }

    private lateinit var viewPagerAdapter: SearchPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.activity_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = SearchPagerAdapter(resources, childFragmentManager)
        pager.adapter = viewPagerAdapter
        pager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(pager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.search)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search)
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
        val searchView = item.actionView as SearchView
        val historyDb = HistorySuggestionListener(
            requireContext(),
            {
                querySubject.onNext(it)
                activity?.hideKeyboard()
            },
            searchView,
        )

        with(searchView) {
            setOnQueryTextListener(historyDb)
            setOnSuggestionListener(historyDb)
            setIconifiedByDefault(false)
            isIconified = false
        }
    }
}
