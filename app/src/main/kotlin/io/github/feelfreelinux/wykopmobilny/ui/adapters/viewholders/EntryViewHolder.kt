package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryWidget
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_layout.view.*


class EntryViewHolder(override val containerView: EntryWidget, val userManagerApi: UserManagerApi, val entryReplyListener : ((Author) -> Unit)?, val settingsPreferencesApi: SettingsPreferencesApi, val entryPresenter: EntryPresenter) : RecyclableViewHolder(containerView), LayoutContainer {
    fun bindView(entry : Entry, enableClickListener : Boolean = true) {
        containerView.apply {
            shouldEnableClickListener = enableClickListener
            replyListener = entryReplyListener
            setEntryData(entry, userManagerApi, settingsPreferencesApi, entryPresenter)
        }
    }

    override fun cleanRecycled() {
            GlideApp.with(containerView).clear(containerView.entryImageView)
    }
}