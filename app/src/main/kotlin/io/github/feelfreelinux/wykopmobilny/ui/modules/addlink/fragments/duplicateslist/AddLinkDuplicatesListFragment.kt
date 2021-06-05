package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.duplicateslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.databinding.AddlinkDuplicatesFragmentBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinksAdapter
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.addlink_duplicates_fragment.*
import javax.inject.Inject

class AddLinkDuplicatesListFragment : BaseFragment(), LinkActionListener {

    companion object {
        fun newInstance() = AddLinkDuplicatesListFragment()
    }

    @Inject
    lateinit var owmContentFilter: OWMContentFilter

    @Inject
    lateinit var settingsPreferencesApi: SettingsPreferencesApi

    @Inject
    lateinit var linksAdapter: LinksAdapter

    override fun dig(link: Link) = Unit

    override fun removeVote(link: Link) = Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        AddlinkDuplicatesFragmentBinding.inflate(inflater, container, false).root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val duplicates = (activity as AddlinkActivity).draft.duplicates?.map { LinkMapper.map(it, owmContentFilter) } ?: emptyList()
        duplicates_list.run {
            prepare()
            adapter = linksAdapter
        }

        linksAdapter.linksActionListener = this
        linksAdapter.addData(duplicates, true)
        linksAdapter.disableLoading()

        confirm_duplicates.setOnClickListener {
            (activity as AddlinkActivity).openDetailsScreen()
        }
    }
}
