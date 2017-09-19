package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.EntryResponse
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getTag
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

const val EXTRA_TAG = "EXTRA_TAG"

fun Context.launchTagActivity(tag : String) {
    val intent = Intent(this, TagActivity::class.java)
    intent.putExtra(EXTRA_TAG, tag)
    startActivity(intent)
}

class TagActivity : BaseActivity(), TagView {
    private lateinit var entryTag : String
    @Inject lateinit var presenter : TagPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(toolbar)
        entryTag = intent.data?.getTag() ?: intent.getStringExtra(EXTRA_TAG)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "#" + entryTag
        }

        WykopApp.uiInjector.inject(this)
        presenter.tag = entryTag
        presenter.subscribe(this)
        feedRecyclerView.presenter = presenter
        feedRecyclerView.initAdapter()
        setSupportActionBar(toolbar)
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) =
        feedRecyclerView.addDataToAdapter(entryList, shouldClearAdapter)
}