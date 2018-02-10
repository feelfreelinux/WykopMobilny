package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryLinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.ObservedTagsAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopNotifier
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_conversations_list.*
import javax.inject.Inject


class MyWykopObservedTagsFragment : BaseFragment(), MyWykopObservedTagsView, SwipeRefreshLayout.OnRefreshListener, MyWykopNotifier {
    @Inject lateinit var adapter : ObservedTagsAdapter
    @Inject lateinit var presenter : MyWykopObservedTagsPresenter
    lateinit var dataFragment : DataFragment<List<ObservedTagResponse>>

    companion object {
        val DATA_FRAGMENT_TAG = "MYWYKOP_OBSERVED_TAGS_FRAGMENT_TAG"

        fun newInstance() : MyWykopObservedTagsFragment {
            return MyWykopObservedTagsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_conversations_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        dataFragment = childFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        presenter.subscribe(this)
        swiperefresh.setOnRefreshListener(this)
        recyclerView?.prepare()
        recyclerView?.adapter = adapter

        if (dataFragment.data != null) {
            adapter.dataset.clear()
            adapter.dataset.addAll(dataFragment.data!!)
            adapter.notifyDataSetChanged()
        } else {
            loadingView?.isVisible = true
            presenter.loadTags()
        }
    }

    override fun onRefresh() {
        if (!swiperefresh.isRefreshing) swiperefresh?.isRefreshing = true
        presenter.loadTags()
    }

    override fun showTags(tags: List<ObservedTagResponse>) {
        loadingView?.isVisible = false
        swiperefresh?.isRefreshing = false
        adapter.dataset.clear()
        adapter.dataset.addAll(tags)
        adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dataFragment.data = adapter.dataset
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) childFragmentManager.removeDataFragment(dataFragment)
    }

    override fun removeDataFragment() {
        childFragmentManager.removeDataFragment(dataFragment)
    }
}