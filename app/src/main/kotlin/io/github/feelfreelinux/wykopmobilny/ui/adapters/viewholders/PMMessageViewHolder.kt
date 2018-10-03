package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.pmmessage_sent_layout.view.*

class PMMessageViewHolder(
    val view: View,
    val linkHandlerApi: WykopLinkHandlerApi,
    val settingsPreferencesApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi
) : RecyclableViewHolder(view) {

    fun bindView(message: PMMessage) {
        flipMessage(message.isSentFromUser)
        view.apply {
            date.text = message.date

            message.app?.let {
                date.text = message.date
            }

            body.prepareBody(message.body, { linkHandlerApi.handleUrl(it) }, null, settingsPreferencesApi.openSpoilersDialog)
            embedImage.forceDisableMinimizedMode = true
            embedImage.setEmbed(message.embed, settingsPreferencesApi, navigatorApi)
        }
    }

    /**
     * This function flips the message depending on the direction.
     */
    private fun flipMessage(isSentFromUser: Boolean) {
        val cardViewParams = view.notificationItem.layoutParams as RelativeLayout.LayoutParams
        // Resolve colors from attr
        val theme = view.getActivityContext()!!.theme
        val usersMessageColor = TypedValue()
        theme?.resolveAttribute(R.attr.usersMessageColor, usersMessageColor, true)

        val usersMessageSubtextDirected = TypedValue()
        theme?.resolveAttribute(R.attr.usersMessageSubtextDirected, usersMessageSubtextDirected, true)

        val cardBackgroundColor = TypedValue()
        theme?.resolveAttribute(R.attr.yourMessageColor, cardBackgroundColor, true)

        val marginVertical = view.resources.getDimension(R.dimen.padding_dp_mini).toInt()
        val marginHorizontal = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_horizontal).toInt()
        val marginFlip = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_flip).toInt()

        if (isSentFromUser) {
            view.notificationItem.setCardBackgroundColor(usersMessageColor.data)
            view.notificationItem.date.setTextColor(usersMessageSubtextDirected.data)
            cardViewParams.setMargins(marginFlip, marginVertical, marginHorizontal, marginVertical)
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_END)
                height = RelativeLayout.LayoutParams.WRAP_CONTENT
            }
        } else {
            view.notificationItem.setCardBackgroundColor(cardBackgroundColor.data)
            view.notificationItem.date.setTextColor(ContextCompat.getColor(view.context, R.color.authorHeader_date_Dark))
            cardViewParams.setMargins(marginHorizontal, marginVertical, marginFlip, marginVertical)
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_START)
                height = RelativeLayout.LayoutParams.WRAP_CONTENT
            }
        }

        view.notificationItem.layoutParams = cardViewParams
    }

    override fun cleanRecycled() {
        view.apply {
            date.text = null
            body.text = null
            GlideApp.with(this).clear(embedImage)
        }
    }
}