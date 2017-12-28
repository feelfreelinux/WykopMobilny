package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import android.support.v7.widget.SearchView
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.utils.hideKeyboard
import io.github.feelfreelinux.wykopmobilny.utils.printout
import kotlinx.android.synthetic.main.activity_search.*


interface SearchFragmentQuery {
    var searchQuery : String
}

interface SearchFragmentNotifier {
    fun notifyQueryChanged()
    fun removeDataFragment()
}

class SearchFragment : BaseFragment(), SearchFragmentQuery {
    override var searchQuery = ""

    private lateinit var viewPagerAdapter : SearchPagerAdapter

    companion object {
        val EXTRA_QUERY = "EXTRA_QUERY"

        fun newInstance() : Fragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.activity_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = SearchPagerAdapter(resources, childFragmentManager)
        pager.adapter = viewPagerAdapter
        pager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(pager)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.search)
        savedInstanceState?.apply{
            if (containsKey(EXTRA_QUERY)) {
                searchQuery = getString(EXTRA_QUERY)
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu.findItem(R.id.action_search)
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
        val searchView = item.actionView as SearchView
        searchView.setQuery(searchQuery, false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (query.length > 2) {
                        searchQuery = query
                        for (i in 0 until viewPagerAdapter.registeredFragments.size()) {
                            (viewPagerAdapter.registeredFragments.valueAt(i) as SearchFragmentNotifier)
                                    .notifyQueryChanged()
                        }
                        activity.hideKeyboard()
                        searchView.clearFocus()
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = false

        })
        searchView.setIconifiedByDefault(false)
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) {
            for (i in 0 until viewPagerAdapter.registeredFragments.size()) {
                (viewPagerAdapter.registeredFragments.valueAt(i) as SearchFragmentNotifier)
                        .removeDataFragment()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_QUERY, searchQuery)
    }
}