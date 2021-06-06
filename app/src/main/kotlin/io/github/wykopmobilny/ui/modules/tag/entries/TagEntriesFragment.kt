package io.github.wykopmobilny.ui.modules.tag.entries

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.api.responses.TagMetaResponse
import io.github.wykopmobilny.base.BaseEntriesFragment
import io.github.wykopmobilny.ui.modules.tag.TagActivityView
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class TagEntriesFragment : BaseEntriesFragment(), TagEntriesView {

    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"

        fun newInstance(tag: String): androidx.fragment.app.Fragment {
            val fragment = TagEntriesFragment()
            val data = Bundle()
            data.putString(EXTRA_TAG, tag)
            fragment.arguments = data
            return fragment
        }
    }

    @Inject
    lateinit var presenter: TagEntriesPresenter

    @Inject
    lateinit var userManager: UserManagerApi

    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }

    private val entryTag by lazy { requireArguments().getString(EXTRA_TAG)!! }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
