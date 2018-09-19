package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.entries

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseEntriesFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.tag.TagActivityView
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class TagEntriesFragment : BaseEntriesFragment(), TagEntriesView {

    companion object {
        const val EXTRA_TAG = "EXTRA_TAG"

        fun newInstance(tag: String): androidx.fragment.app.Fragment {
            val fragment = TagEntriesFragment()
            val data = Bundle()
            data.putString(EXTRA_TAG, tag)
            fragment.arguments = data
            return fragment
        }
    }

    @Inject lateinit var presenter: TagEntriesPresenter
    @Inject lateinit var userManager: UserManagerApi

    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }

    private val entryTag by lazy { arguments!!.getString(EXTRA_TAG) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.tag = entryTag
        entriesAdapter.entryActionListener = presenter
        entriesAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    override fun setParentMeta(tagMeta: TagMetaResponse) {
        (activity as TagActivityView).setMeta(tagMeta)
    }
}