package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryWidget
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.entry_layout.*
import kotlinx.android.synthetic.main.entry_list_item.view.*


class EntryViewHolder(override val containerView: View, val userManagerApi: UserManagerApi, val entryReplyListener : ((Author) -> Unit)?, val settingsPreferencesApi: SettingsPreferencesApi, val entryPresenter: EntryPresenter) : RecyclableViewHolder(containerView), LayoutContainer {
    fun bindView(entry : Entry, enableClickListener : Boolean = true) {
        containerView.apply {
            entryWidget.apply {
                shouldEnableClickListener = enableClickListener
                replyListener = entryReplyListener
                setEntryData(entry, userManagerApi, settingsPreferencesApi, entryPresenter)
            }

            entryWidget.isVisible = !(entry.isBlocked && enableClickListener)
            showHiddenTextView.isVisible = (entry.isBlocked && enableClickListener)
            if ((entry.isBlocked && enableClickListener)) {
                val text = SpannableString("Pokaż ukryty wpis od @" + entry.author.nick)
                text.setSpan(ForegroundColorSpan(getGroupColor(entry.author.group)), text.length-(entry.author.nick.length+1), text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                showHiddenTextView.setText(text, TextView.BufferType.SPANNABLE)
            }

            showHiddenTextView.setOnClickListener {
                entry.isBlocked = false
                entryWidget.isVisible = true
                showHiddenTextView.isVisible = false
            }
        }
    }

    override fun cleanRecycled() {
            GlideApp.with(containerView).clear(containerView.entryWidget.entryImageView)
    }
}