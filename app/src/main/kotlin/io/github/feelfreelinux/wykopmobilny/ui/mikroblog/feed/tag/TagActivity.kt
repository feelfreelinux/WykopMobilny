package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.utils.api.getTag
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

fun Context.launchTagActivity(tag : String) {
    val intent = Intent(this, TagActivity::class.java)
    intent.putExtra(TagActivity.EXTRA_TAG, tag)
    startActivity(intent)
}

class TagActivity : BaseActivity(), TagView {
    private lateinit var entryTag : String
    lateinit var tagDataFragment : DataFragment<List<Entry>>
    @Inject lateinit var presenter : TagPresenter

    companion object {
        val EXTRA_TAG = "EXTRA_TAG"
        val EXTRA_TAG_DATA_FRAGMENT = "DATA_FRAGMENT_#"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        setSupportActionBar(toolbar)
        entryTag = intent.data?.getTag() ?: intent.getStringExtra(EXTRA_TAG)
        tagDataFragment = supportFragmentManager.getDataFragmentInstance(EXTRA_TAG_DATA_FRAGMENT + entryTag)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "#" + entryTag
        }

        WykopApp.uiInjector.inject(this)
        presenter.tag = entryTag
        presenter.subscribe(this)
        feedRecyclerView.presenter = presenter
        feedRecyclerView.initAdapter(tagDataFragment.data)
        setSupportActionBar(toolbar)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        tagDataFragment.data = feedRecyclerView.entries
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) supportFragmentManager.removeDataFragment(tagDataFragment)
    }

    override fun addDataToAdapter(entryList: List<Entry>, shouldClearAdapter: Boolean) =
        feedRecyclerView.addDataToAdapter(entryList, shouldClearAdapter)
}