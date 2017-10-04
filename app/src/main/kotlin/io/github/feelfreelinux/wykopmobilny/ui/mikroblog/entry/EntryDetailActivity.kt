package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.view.MenuItem
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.decorators.EntryCommentItemDecoration
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.input.entry.comment.add.createNewEntryComment
import io.github.feelfreelinux.wykopmobilny.utils.api.getWpisId
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.activity_entry.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

fun Context.openEntryActivity(entryId : Int) {
    val intent = Intent(this, EntryActivity::class.java)
    intent.putExtra(EntryActivity.EXTRA_ENTRY_ID, entryId)
    startActivity(intent)
}

class EntryActivity : BaseActivity(), EntryDetailView, SwipeRefreshLayout.OnRefreshListener {
    var entryId = 0
    companion object {
        val EXTRA_ENTRY_ID = "ENTRY_ID"
        val EXTRA_FRAGMENT_KEY = "ENTRY_ACTIVITY_#"
    }

    @Inject lateinit var presenter : EntryDetailPresenter
    private lateinit var entryFragmentData : DataFragment<Entry>
    private val adapter by lazy { EntryDetailAdapter() }

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

        // Prepare InputToolbar
        inputToolbar.sendPhotoListener = {
            photo, body -> presenter.addComment(body, photo)
        }

        inputToolbar.sendPhotoUrlListener = {
            photo, body -> presenter.addComment(body, photo)
        }


        swiperefresh.setOnRefreshListener(this)
        entryFragmentData = supportFragmentManager.getDataFragmentInstance(EXTRA_FRAGMENT_KEY + entryId)

        if (entryFragmentData.data != null)
            adapter.entry = entryFragmentData.data
        else {
            // Trigger data loading
            loadingView.isVisible = true
            presenter.loadData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.entry_fragment_menu, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        entryFragmentData.data = adapter.entry
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(entryFragmentData)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reply -> adapter.entry?.let { createNewEntryComment(entryId, adapter.entry!!.author.nick) }
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onRefresh() { presenter.loadData() }

    override fun showEntry(entry: Entry) {
        adapter.entry = entry
        loadingView.isVisible = false
        swiperefresh.isRefreshing = false
        adapter.notifyDataSetChanged()
    }

    override fun hideInputbarProgress() {
        inputToolbar.showProgress(false)
    }

    override fun resetInputbarState() {
        inputToolbar.resetState()
    }
}