package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.links

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BaseLinksFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.tag.TagActivityView
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class TagLinksFragment : BaseLinksFragment(), TagLinksView {
    @Inject
    lateinit var presenter: TagLinksPresenter
    override var loadDataListener: (Boolean) -> Unit = {
        presenter.loadData(it)
    }

    val tagString by lazy { arguments!!.getString(EXTRA_TAG) }

    @Inject
    lateinit var userManager: UserManagerApi

    companion object {
        val DATA_FRAGMENT_TAG = "TAG_DATA_FRAGMENT"
        val EXTRA_TAG = "EXTRA_TAG"

        fun newInstance(tag: String): androidx.fragment.app.Fragment {
            val fragment = TagLinksFragment()
            val data = Bundle()
            data.putString(EXTRA_TAG, tag)
            fragment.arguments = data
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
        presenter.tag = tagString
        linksAdapter.linksActionListener = presenter
        linksAdapter.loadNewDataListener = { loadDataListener(false) }
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

    override fun setParentMeta(tagMetaResponse: TagMetaResponse) {
        (activity as TagActivityView).setMeta(tagMetaResponse)
    }
}