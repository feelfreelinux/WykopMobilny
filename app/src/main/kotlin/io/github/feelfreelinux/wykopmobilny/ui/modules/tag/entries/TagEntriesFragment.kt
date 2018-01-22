package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.entries

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.ui.adapters.FeedAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.tag.TagActivityView
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class TagEntriesFragment : BaseFeedFragment<Entry>(), TagEntriesView {
    @Inject lateinit var presenter : TagEntriesPresenter
    @Inject override lateinit var feedAdapter : FeedAdapter
    val entryTag by lazy { arguments!!.getString(EXTRA_TAG) }
    lateinit var tagDataFragment : DataFragment<PagedDataModel<List<Entry>>>
    @Inject lateinit var userManager : UserManagerApi


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.tag = entryTag

        tagDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG + entryTag)
        tagDataFragment.data?.apply {
            presenter.page = page
        }

        presenter.subscribe(this)
        initAdapter(tagDataFragment.data?.model)
    }

    companion object {
        val DATA_FRAGMENT_TAG = "TAG_DATA_FRAGMENT"
        val EXTRA_TAG = "EXTRA_TAG"

        fun newInstance(tag : String) : Fragment {
            val fragment = TagEntriesFragment()
            val data = Bundle()
            data.putString(EXTRA_TAG, tag)
            fragment.arguments = data
            return fragment
        }
    }

    override fun setParentMeta(tagMeta: TagMetaResponse) {
        (activity as TagActivityView).setMeta(tagMeta)
    }
    override fun loadData(shouldRefresh: Boolean) {
        presenter.loadData(shouldRefresh)
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tagDataFragment.data = PagedDataModel(presenter.page , data)
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) supportFragmentManager.removeDataFragment(tagDataFragment)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }
}