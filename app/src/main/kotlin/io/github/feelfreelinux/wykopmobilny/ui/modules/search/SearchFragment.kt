package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.utils.hideKeyboard
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import br.com.mauker.materialsearchview.MaterialSearchView
import io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import kotlinx.android.synthetic.main.activity_navigation.*


class SearchFragment : BaseFragment() {
    val querySubject by lazy { PublishSubject.create<String>() }

    private lateinit var viewPagerAdapter : SearchPagerAdapter

    val searchView by lazy { (activity!! as MainNavigationActivity).searchView }


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = SearchPagerAdapter(resources, childFragmentManager)
        pager.adapter = viewPagerAdapter
        pager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(pager)



        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (query.length > 2) {
                        querySubject.onNext(query)
                        searchView.addSuggestion(query)
                        activity?.hideKeyboard()
                        searchView.clearFocus()
                    }

                    searchView.closeSearch()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = false

        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).supportActionBar?.setTitle(R.string.search)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                searchView.openSearch()
            }
        }
        return false
    }
}