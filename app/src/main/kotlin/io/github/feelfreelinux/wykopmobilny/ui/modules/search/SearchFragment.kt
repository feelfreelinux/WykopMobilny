package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import android.support.v7.widget.SearchView
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*


interface SearchFragmentQuery {
    var searchQuery : String
}

class SearchFragment : BaseFragment(), SearchView.OnQueryTextListener, SearchFragmentQuery {
    override var searchQuery = ""
    lateinit var viewPagerAdapter : SearchPagerAdapter
    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            if (query.length > 2) {
                searchQuery = query
                viewPagerAdapter.notifyDataSetChanged()
            }
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    companion object {
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
        searchView.setOnQueryTextListener(this)
        searchView.setIconifiedByDefault(false)
    }
}