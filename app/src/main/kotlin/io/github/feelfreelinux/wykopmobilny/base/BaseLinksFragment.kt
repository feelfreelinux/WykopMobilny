package io.github.feelfreelinux.wykopmobilny.base

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinksAdapter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.VotersDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksFragmentView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.entries_fragment.*
import kotlinx.android.synthetic.main.search_empty_view.*
import javax.inject.Inject

open class BaseLinksFragment : BaseFragment(), LinksFragmentView, androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {
    var showSearchEmptyView: Boolean
        get() = searchEmptyView.isVisible
        set(value) {
            searchEmptyView.isVisible = value
        }

    open var loadDataListener : (Boolean) -> Unit = {}

    @Inject lateinit var linksApi: LinksApi
    val subjectDisposable by lazy { CompositeDisposable() }

    @Inject
    lateinit var linksAdapter : LinksAdapter

    override fun onRefresh() {
        loadDataListener(true)
    }

    // Inflate view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.entries_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        linksAdapter.loadNewDataListener = { loadDataListener(false) }

        // Setup views
        swipeRefresh.setOnRefreshListener(this)
        recyclerView.run {
            prepare()
            adapter = linksAdapter
        }

        loadingView.isVisible = true

        val schedulers = WykopSchedulers()
        subjectDisposable.addAll(
                linksApi.digSubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ updateLinkVoteState(it.linkId, it.voteResponse.buries, it.voteResponse.diggs, "dig") }),
                linksApi.burySubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ updateLinkVoteState(it.linkId, it.voteResponse.buries, it.voteResponse.diggs, "bury") }),
                linksApi.voteRemoveSubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ updateLinkVoteState(it.linkId, it.voteResponse.buries, it.voteResponse.diggs, null) })
        )
    }

    private fun updateLinkVoteState(linkId : Int, buryCount : Int, voteCount : Int, userVote : String?) {
        linksAdapter.data.firstOrNull { it.id == linkId }?.apply {
            this.buryCount = buryCount
            this.voteCount = voteCount
            this.userVote = userVote
            linksAdapter.updateLink(this)
        }
    }

    /**
     * Removes progressbar from adapter
     */
    override fun disableLoading() {
        linksAdapter.disableLoading()
    }

    /**
     * Use this function to add items to EntriesFragment
     * @param items List of entries to add
     * @param shouldRefresh If true adapter will refresh its data with provided items. False by default
     */
    override fun addItems(items : List<Link>, shouldRefresh : Boolean) {
        linksAdapter.addData(items, shouldRefresh)
        swipeRefresh?.isRefreshing = false
        loadingView?.isVisible = false

        // Scroll to top if refreshing list
        if (shouldRefresh) {
            (recyclerView?.layoutManager as? androidx.recyclerview.widget.LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun updateLink(link: Link) {
        linksAdapter.updateLink(link)
    }

    override fun onDestroy() {
        super.onDestroy()
        subjectDisposable.dispose()
    }
}