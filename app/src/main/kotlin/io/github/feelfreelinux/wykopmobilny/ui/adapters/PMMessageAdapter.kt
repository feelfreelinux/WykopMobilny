package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.PMMessageViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class PMMessageAdapter @Inject constructor(
    val settingsPreferencesApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : androidx.recyclerview.widget.RecyclerView.Adapter<PMMessageViewHolder>() {

    val messages: ArrayList<PMMessage> = arrayListOf()

    override fun getItemCount() = messages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PMMessageViewHolder = PMMessageViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.pmmessage_sent_layout, parent, false),
        linkHandlerApi,
        settingsPreferencesApi,
        navigatorApi
    )

    override fun onBindViewHolder(holder: PMMessageViewHolder, position: Int) =
        holder.bindView(messages[position])

    override fun onViewRecycled(holder: PMMessageViewHolder) {
        (holder as? PMMessageViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}