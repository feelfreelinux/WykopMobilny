package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.pmmessage_sent_layout.view.*

class PMMessageViewHolder(val view: View,
                          val linkHandlerApi: WykopLinkHandlerApi,
                          val settingsPreferencesApi: SettingsPreferencesApi,
                          val navigatorApi: NewNavigatorApi) : RecyclableViewHolder(view) {

    fun bindView(message: PMMessage) {
        flipMessage(message.isSentFromUser)
        view.apply {
            date.text = message.date

            message.app?.let {
                date.text = context.getString(R.string.date_with_user_app, message.date, message.app)
            }

            body.prepareBody(message.body, { linkHandlerApi.handleUrl(it) })
            embedImage.forceDisableMinimizedMode = true
            embedImage.setEmbed(message.embed, settingsPreferencesApi, navigatorApi)
        }
    }

    /**
     * This function flips the message depending on the direction.
     */
    fun flipMessage(isSentFromUser: Boolean) {
        val cardViewParams = view.cardView.layoutParams as RelativeLayout.LayoutParams
        // Resolve colors from attr
        val theme = view.getActivityContext()!!.theme
        val usersMessageColor = TypedValue()
        theme?.resolveAttribute(R.attr.usersMessageColor, usersMessageColor, true)

        val usersMessageSubtextDirected = TypedValue()
        theme?.resolveAttribute(R.attr.usersMessageSubtextDirected, usersMessageSubtextDirected, true)

        val cardBackgroundColor = TypedValue()
        theme?.resolveAttribute(R.attr.cardViewColor, cardBackgroundColor, true)

        val marginVertical = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_vertical).toInt()
        val marginHorizontal = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_horizontal).toInt()
        val marginFlip = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_flip).toInt()

        if (isSentFromUser) {
            view.cardView.setCardBackgroundColor(usersMessageColor.data)
            view.cardView.date.setTextColor(usersMessageSubtextDirected.data)
            cardViewParams.setMargins(marginFlip, marginVertical, marginHorizontal, marginVertical)
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_END)
                height = RelativeLayout.LayoutParams.WRAP_CONTENT
            }
        } else {
            view.cardView.setCardBackgroundColor(cardBackgroundColor.data)
            view.cardView.date.setTextColor(ContextCompat.getColor(view.context, R.color.authorHeader_date_Dark))
            cardViewParams.setMargins(marginHorizontal, marginVertical, marginFlip, marginVertical)
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_START)
                height = RelativeLayout.LayoutParams.WRAP_CONTENT
            }
        }

        view.cardView.layoutParams = cardViewParams
    }

    override fun cleanRecycled() {
        view.apply {
            date.text = null
            body.text = null
            GlideApp.with(this).clear(embedImage)
        }
    }
}