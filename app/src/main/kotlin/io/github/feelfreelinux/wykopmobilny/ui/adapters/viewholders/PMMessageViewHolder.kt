package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import kotlinx.android.synthetic.main.pmmessage_sent_layout.view.*

class PMMessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val linkHandler by lazy { WykopLinkHandler(view.context) }

    fun bindView(message : PMMessage) {
        flipMessage(message.isSentFromUser)

        view.apply {
            avatarView.setAuthor(message.author)
            val prettyDate = message.date.toPrettyDate()
            date.text = prettyDate

            message.author.app?.let {
                date.text = context.getString(R.string.date_with_user_app, prettyDate, message.author.app)
            }

            body.prepareBody(message.body, linkHandler)
            embedImage.setEmbed(message.embed)
        }
    }

    /**
     * This function flips the message depending on the direction.
     */
    fun flipMessage(isSentFromUser : Boolean) {
        val avatarParams = view.avatarView.layoutParams as RelativeLayout.LayoutParams

        if (isSentFromUser) {
            avatarParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_END)
            }
        } else {
            avatarParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_START)
            }
        }

        view.avatarView.layoutParams = avatarParams

        val cardViewParams = view.cardView.layoutParams as RelativeLayout.LayoutParams

        if (isSentFromUser) {
            cardViewParams.apply {
                removeRule(RelativeLayout.END_OF)
                addRule(RelativeLayout.START_OF, R.id.avatarView)
            }
        } else {
            cardViewParams.apply {
                removeRule(RelativeLayout.START_OF)
                addRule(RelativeLayout.END_OF, R.id.avatarView)
            }
        }

        view.cardView.layoutParams = cardViewParams
    }
}