package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.entries

import android.os.Bundle
import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.tag.TagActivityView
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class TagEntriesFragment : BaseEntriesFragment(), TagEntriesView {
    @Inject
    lateinit var presenter: TagEntriesPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    val entryTag by lazy { arguments!!.getString(EXTRA_TAG) }

    @Inject
    lateinit var userManager: UserManagerApi

    companion object {
        val DATA_FRAGMENT_TAG = "TAG_DATA_FRAGMENT"
        val EXTRA_TAG = "EXTRA_TAG"

        fun newInstance(tag: String): Fragment {
            val fragment = TagEntriesFragment()
            val data = Bundle()
            data.putString(EXTRA_TAG, tag)
            fragment.arguments = data
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.tag = entryTag
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun setParentMeta(tagMeta: TagMetaResponse) {
        (activity as TagActivityView).setMeta(tagMeta)
    }
}