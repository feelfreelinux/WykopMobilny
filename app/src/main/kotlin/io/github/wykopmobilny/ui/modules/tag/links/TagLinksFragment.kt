package io.github.wykopmobilny.ui.modules.tag.links

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.api.responses.TagMetaResponse
import io.github.wykopmobilny.base.BaseLinksFragment
import io.github.wykopmobilny.ui.modules.tag.TagActivityView
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class TagLinksFragment : BaseLinksFragment(), TagLinksView {

    companion object {
        const val DATA_FRAGMENT_TAG = "TAG_DATA_FRAGMENT"
        const val EXTRA_TAG = "EXTRA_TAG"

        fun newInstance(tag: String): androidx.fragment.app.Fragment {
            val fragment = TagLinksFragment()
            val data = Bundle()
            data.putString(EXTRA_TAG, tag)
            fragment.arguments = data
            return fragment
        }
    }

    @Inject
    lateinit var presenter: TagLinksPresenter

    @Inject
    lateinit var userManager: UserManagerApi

    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }

    private val tagString by lazy { requireArguments().getString(EXTRA_TAG) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.subscribe(this)
        presenter.tag = tagString!!
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
        presenter.loadData(true)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        super.onDestroy()
    }

    override fun setParentMeta(tagMetaResponse: TagMetaResponse) {
        (activity as TagActivityView).setMeta(tagMetaResponse)
    }
}
