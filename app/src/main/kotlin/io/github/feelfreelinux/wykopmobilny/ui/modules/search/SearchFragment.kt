package io.github.feelfreelinux.wykopmobilny.ui.modules.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.databinding.ActivitySearchBinding
import io.github.feelfreelinux.wykopmobilny.utils.hideKeyboard
import io.github.feelfreelinux.wykopmobilny.utils.viewBinding
import io.reactivex.subjects.PublishSubject

class SearchFragment : BaseFragment(R.layout.activity_search) {

    companion object {
        fun newInstance() = SearchFragment()
    }

    val querySubject by lazy { PublishSubject.create<String>() }

    private lateinit var viewPagerAdapter: SearchPagerAdapter

    private val binding by viewBinding(ActivitySearchBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = SearchPagerAdapter(resources, childFragmentManager)
        binding.pager.adapter = viewPagerAdapter
        binding.pager.offscreenPageLimit = 3
        binding.tabLayout.setupWithViewPager(binding.pager)

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
