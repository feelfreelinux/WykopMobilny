package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries

import android.os.Bundle
import android.support.v4.app.ActivityCompat.invalidateOptionsMenu
import android.support.v4.app.Fragment
import android.view.*
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.EntryFeedFragment
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class TagEntriesFragment : EntryFeedFragment(), TagEntriesView {
    @Inject lateinit var presenter : TagEntriesPresenter
    val entryTag by lazy { arguments.getString(EXTRA_TAG) }
    lateinit var tagDataFragment : DataFragment<PagedDataModel<List<Entry>>>
    @Inject lateinit var userManager : UserManagerApi
    private var tagMeta : TagMetaResponse? = null
    @Inject lateinit var navigatorApi : NavigatorApi

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.tag = entryTag

        tagDataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG + entryTag)
        tagDataFragment.data?.apply {
            presenter.page = page
            presenter.tag = arguments.getString(entryTag)
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

    override fun loadData(shouldRefresh: Boolean) {
        presenter.loadData(shouldRefresh)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.tag_menu, menu)

        if (userManager.isUserAuthorized()) {
            tagMeta?.apply {
                menu.apply {
                    if (isObserved) {
                        findItem(R.id.action_unobserve).isVisible = true
                    } else if (!isBlocked) {
                        findItem(R.id.action_observe).isVisible = true
                        findItem(R.id.action_block).isVisible = true
                    } else if (isBlocked) {
                        findItem(R.id.action_unblock).isVisible = true
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_observe -> presenter.observeTag()
            R.id.action_unobserve -> presenter.unobserveTag()
            R.id.action_block -> presenter.blockTag()
            R.id.action_unblock -> presenter.unblockTag()
            android.R.id.home -> activity.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        tagDataFragment.data = PagedDataModel(presenter.page , entries)
    }

    override fun onPause() {
        super.onPause()
        if (isRemoving) fragmentManager.removeDataFragment(tagDataFragment)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

    // This thing will be moved to TagActivity @TODO

    override fun setMeta(tagMeta: TagMetaResponse) {
        this.tagMeta = tagMeta
        invalidateOptionsMenu(activity)
    }

    override fun setObserveState(tagState: TagStateResponse) {
        tagMeta?.isBlocked = tagState.isBlocked
        tagMeta?.isObserved = tagState.isObserved
        invalidateOptionsMenu(activity)
    }
}