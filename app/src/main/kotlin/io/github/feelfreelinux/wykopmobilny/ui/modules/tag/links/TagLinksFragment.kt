package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.links

import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.tag.TagActivityView
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
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

    @Inject lateinit var presenter: TagLinksPresenter
    @Inject lateinit var userManager: UserManagerApi

    override var loadDataListener: (Boolean) -> Unit = { presenter.loadData(it) }

    private val tagString by lazy { arguments!!.getString(EXTRA_TAG) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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