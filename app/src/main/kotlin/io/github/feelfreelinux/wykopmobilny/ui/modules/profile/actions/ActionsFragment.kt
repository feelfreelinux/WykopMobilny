package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryLinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.feed_fragment.*
import javax.inject.Inject

class ActionsFragment : BaseFragment(), ActionsView {
    val username by lazy { (activity as ProfileActivity).username }
    companion object {
        val DATA_FRAGMENT_TAG = "ACTION_FRAGMENT_TAG"

        fun newInstance() : ActionsFragment {
            val fragment = ActionsFragment()
            return fragment
        }
    }

    @Inject lateinit var feedAdapter : EntryLinkAdapter
    @Inject lateinit var presenter : ActionsFragmentPresenter
    lateinit var dataFragment : DataFragment<List<EntryLink>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.username = username
        dataFragment = childFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        presenter.subscribe(this)
        recyclerView.prepare()
        recyclerView.adapter = feedAdapter
        swiperefresh.setOnRefreshListener { loadData() }
        if (dataFragment.data != null) {
            feedAdapter.addData(dataFragment.data!!, true)
            loadingView.isVisible = false
            feedAdapter.disableLoading()
        } else {
            loadData()
            loadingView.isVisible = true
        }
    }
    fun loadData() {
        presenter.getActions()
    }

    override fun showActions(links: List<EntryLink>) {
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        feedAdapter.addData(links, true)
        feedAdapter.disableLoading()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dataFragment.data =feedAdapter.data
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) childFragmentManager.removeDataFragment(dataFragment)
    }
}