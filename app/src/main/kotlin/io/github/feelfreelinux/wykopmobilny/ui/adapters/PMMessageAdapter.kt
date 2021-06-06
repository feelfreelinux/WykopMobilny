package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.databinding.PmmessageSentLayoutBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.PMMessageViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class PMMessageAdapter @Inject constructor(
    private val settingsPreferencesApi: SettingsPreferencesApi,
    private val navigatorApi: NewNavigatorApi,
    private val linkHandlerApi: WykopLinkHandlerApi
) : RecyclerView.Adapter<PMMessageViewHolder>() {

    val messages: ArrayList<PMMessage> = arrayListOf()

    override fun getItemCount() = messages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PMMessageViewHolder = PMMessageViewHolder(
        PmmessageSentLayoutBinding.inflate(parent.layoutInflater, parent, false),
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
