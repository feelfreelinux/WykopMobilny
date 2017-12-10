package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users

import android.content.Context
import android.os.Bundle
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.base.BaseFeedFragment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.fragments.DataFragment
import io.github.feelfreelinux.wykopmobilny.models.fragments.PagedDataModel
import io.github.feelfreelinux.wykopmobilny.models.fragments.getDataFragmentInstance
import io.github.feelfreelinux.wykopmobilny.models.fragments.removeDataFragment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryLinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopView
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.tags.MyWykopTagsPresenter
import javax.inject.Inject

class MyWykopUsersFragment : BaseFeedFragment<EntryLink>(), MyWykopView {
    override val feedAdapter by lazy { EntryLinkAdapter() }
    @Inject lateinit var presenter : MyWykopUsersPresenter
    lateinit var dataFragment : DataFragment<PagedDataModel<List<EntryLink>>>

    companion object {
        val DATA_FRAGMENT_TAG = "MYWYKOP_USERS_FRAGMENT_TAG"

        fun newInstance() : MyWykopUsersFragment {
            return MyWykopUsersFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        dataFragment = supportFragmentManager.getDataFragmentInstance(DATA_FRAGMENT_TAG)
        dataFragment.data?.apply {
            presenter.page = page
        }
        presenter.subscribe(this)
        initAdapter(dataFragment.data?.model)
    }

    override fun loadData(shouldRefresh: Boolean) {
        presenter.loadData(shouldRefresh)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        dataFragment.data = PagedDataModel(presenter.page , data)
    }
    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }
    
    override fun onPause() {
        super.onPause()
        if (isRemoving) fragmentManager.removeDataFragment(dataFragment)
    }
}