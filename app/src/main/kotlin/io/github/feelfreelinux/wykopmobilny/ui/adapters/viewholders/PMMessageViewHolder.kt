package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.pmmessage_sent_layout.view.*
import javax.inject.Inject

class PMMessageViewHolder(val view: View) : RecyclerView.ViewHolder(view), URLClickedListener {
    @Inject lateinit var linkHandler: WykopLinkHandlerApi

    override fun handleUrl(url: String) {
        linkHandler.handleUrl(view.getActivityContext()!!, url)
    }

    init {
        WykopApp.uiInjector.inject(this)
    }

    fun bindView(message: PMMessage) {
        flipMessage(message.isSentFromUser)

        view.apply {
            val prettyDate = message.date.toPrettyDate()
            date.text = prettyDate

            message.app?.let {
                date.text = context.getString(R.string.date_with_user_app, prettyDate, message.app)
            }

            body.prepareBody(message.body, this@PMMessageViewHolder)
            embedImage.setEmbed(message.embed)
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

        val margin_vertical = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_vertical).toInt()
        val margin_horizontal = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_horizontal).toInt()
        val margin_flip = view.resources.getDimension(R.dimen.pmmessage_sent_layout_card_margin_flip).toInt()

        if (isSentFromUser) {
            view.cardView.setCardBackgroundColor(usersMessageColor.data)
            view.cardView.date.setTextColor(usersMessageSubtextDirected.data)
            cardViewParams.setMargins(margin_flip, margin_vertical, margin_horizontal, margin_vertical)
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_END)
            }
        } else {
            view.cardView.setCardBackgroundColor(cardBackgroundColor.data)
            view.cardView.date.setTextColor(ContextCompat.getColor(view.context, R.color.authorHeader_date_Dark))
            cardViewParams.setMargins(margin_horizontal, margin_vertical, margin_flip, margin_vertical)
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_START)
            }
        }

        view.cardView.layoutParams = cardViewParams
    }
}