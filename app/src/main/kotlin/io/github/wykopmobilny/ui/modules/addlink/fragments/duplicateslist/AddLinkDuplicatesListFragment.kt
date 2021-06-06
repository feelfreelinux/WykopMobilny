package io.github.wykopmobilny.ui.modules.addlink.fragments.duplicateslist

import android.os.Bundle
import android.view.View
import io.github.wykopmobilny.R
import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.base.BaseFragment
import io.github.wykopmobilny.databinding.AddlinkDuplicatesFragmentBinding
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.wykopmobilny.ui.adapters.LinksAdapter
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.wykopmobilny.utils.prepare
import io.github.wykopmobilny.utils.viewBinding
import javax.inject.Inject

class AddLinkDuplicatesListFragment : BaseFragment(R.layout.addlink_duplicates_fragment), LinkActionListener {

    companion object {
        fun newInstance() = AddLinkDuplicatesListFragment()
    }

    @Inject
    lateinit var owmContentFilter: OWMContentFilter

    @Inject
    lateinit var settingsPreferencesApi: SettingsPreferencesApi

    @Inject
    lateinit var linksAdapter: LinksAdapter

    private val binding by viewBinding(AddlinkDuplicatesFragmentBinding::bind)

    override fun dig(link: Link) = Unit

    override fun removeVote(link: Link) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val duplicates = (activity as AddlinkActivity).draft.duplicates?.map { LinkMapper.map(it, owmContentFilter) } ?: emptyList()
        binding.duplicatesList.run {
            prepare()
            adapter = linksAdapter
        }

        linksAdapter.linksActionListener = this
        linksAdapter.addData(duplicates, true)
        linksAdapter.disableLoading()

        binding.confirmDuplicates.setOnClickListener { (activity as AddlinkActivity).openDetailsScreen() }
    }
}
