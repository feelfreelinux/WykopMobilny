package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenter
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.entry_layout.view.*
import kotlinx.android.synthetic.main.entry_list_item.view.*


class EntryViewHolder(val view: View, val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi, val entryPresenter: EntryPresenter) : RecyclableViewHolder(view) {
    fun bindView(entry : Entry, enableClickListener : Boolean = true) {
        view.entry.apply {
            shouldEnableClickListener = enableClickListener
            setEntryData(entry, userManagerApi, settingsPreferencesApi, entryPresenter)
        }
    }

    override fun cleanRecycled() {
        view.apply {
            GlideApp.with(this).clear(view.entry.entryImageView)
        }
    }
}