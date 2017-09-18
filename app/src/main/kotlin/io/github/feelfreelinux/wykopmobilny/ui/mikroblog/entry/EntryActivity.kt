package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.decorators.EntryCommentItemDecoration
import io.github.feelfreelinux.wykopmobilny.ui.add_user_input.launchEntryCommentUserInput
import io.github.feelfreelinux.wykopmobilny.utils.api.getWpisId
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandlerImpl
import kotlinx.android.synthetic.main.feed_recyclerview.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

fun Context.openEntryActivity(entryId : Int) {
    val intent = Intent(this, EntryActivity::class.java)
    intent.putExtra(EXTRA_ENTRY_ID, entryId)
    startActivity(intent)
}

val EXTRA_ENTRY_ID = "ENTRY_ID"
class EntryActivity : BaseActivity(), EntryView, SwipeRefreshLayout.OnRefreshListener {
    var entryId = 0

    @Inject lateinit var presenter : EntryPresenter
    val wykopActions by lazy { WykopActionHandlerImpl(this) as WykopActionHandler }
    private val adapter by lazy { EntryAdapter(wykopActions) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        setSupportActionBar(toolbar)
        entryId = intent.data?.getWpisId() ?: intent.getIntExtra(EXTRA_ENTRY_ID, -1)

        supportActionBar?.apply {
            title = null
            setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = null
        WykopApp.uiInjector.inject(this)
        presenter.entryId = entryId
        presenter.subscribe(this)

        // Prepare RecyclerView
        recyclerView.apply {
            prepare()
            // Set margin, adapter
            addItemDecoration(EntryCommentItemDecoration(resources.getDimensionPixelOffset(R.dimen.comment_section_left_margin)))
            this.adapter = this@EntryActivity.adapter
        }

        // Set needed flags
        loadingView.isVisible = true
        swiperefresh.setOnRefreshListener(this)

        // Trigger data loading
        presenter.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.entry_fragment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reply -> adapter.entry?.let { launchEntryCommentUserInput(entryId, adapter.entry?.author) }
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onRefresh() {presenter.loadData()}

    override fun showEntry(entry: Entry) {
        adapter.entry = entry
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        adapter.notifyDataSetChanged()
    }
}