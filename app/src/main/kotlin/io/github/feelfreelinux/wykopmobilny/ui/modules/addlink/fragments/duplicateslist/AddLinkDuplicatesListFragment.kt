package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.duplicateslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseFragment
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.LinkMapper
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.AddlinkActivity
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.prepare
import kotlinx.android.synthetic.main.addlink_duplicates_fragment.*
import javax.inject.Inject

class AddLinkDuplicatesListFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.addlink_duplicates_fragment, container, false)
    }

    companion object {
        fun newInstance() : AddLinkDuplicatesListFragment {
            return AddLinkDuplicatesListFragment()
        }
    }

    @Inject lateinit var blacklistPreferenceApi : BlacklistPreferencesApi
    @Inject lateinit var settingsPreferencesApi : SettingsPreferencesApi
    @Inject lateinit var linkPreferencesApi : LinksPreferencesApi

    @Inject lateinit var linksAdapter : LinkAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        duplicates_list.apply {
            prepare()
            adapter = linksAdapter
        }
        val duplicates = (activity as AddlinkActivity).draft.duplicates?.map { LinkMapper.map(it, linkPreferencesApi, blacklistPreferenceApi, settingsPreferencesApi) }
        linksAdapter.addData(duplicates!!, true)
        linksAdapter.disableLoading()

        confirm_duplicates.setOnClickListener {
            (activity as AddlinkActivity).openDetailsScreen()
        }
    }
}