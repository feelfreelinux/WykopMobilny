package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.utils.instanceValue
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.toolbar.*

const val EXTRA_TAG = "EXTRA_TAG"

fun Context.launchTagActivity(tag : String) {
    val intent = Intent(this, TagActivity::class.java)
    intent.putExtra(EXTRA_TAG, tag)
    startActivity(intent)
}

class TagActivity : BaseActivity(), TagView {
    private val entryTag by lazy { intent.getStringExtra(EXTRA_TAG) }
    val presenter by lazy { TagPresenter(kodein.instanceValue(), entryTag) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "#" + entryTag
        }

        presenter.subscribe(this)
        feedRecyclerView.presenter = presenter
        feedRecyclerView.initAdapter()
        setSupportActionBar(toolbar)
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) =
        feedRecyclerView.addDataToAdapter(entryList, shouldClearAdapter)
}