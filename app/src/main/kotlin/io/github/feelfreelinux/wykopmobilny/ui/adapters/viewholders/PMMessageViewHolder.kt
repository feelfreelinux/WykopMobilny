package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RelativeLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.pmmessage_sent_layout.view.*
import javax.inject.Inject

class PMMessageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    @Inject lateinit var linkHandler: WykopLinkHandlerApi

    init {
        WykopApp.uiInjector.inject(this)
    }

    fun bindView(message: PMMessage) {
        flipMessage(message.isSentFromUser)

        view.apply {
            val prettyDate = message.date.toPrettyDate()
            date.text = prettyDate
            body.prepareBody(message.body, { linkHandler.handleUrl(context as Activity, it) })
            embedImage.setEmbed(message.embed)
        }
    }

    /**
     * This function flips the message depending on the direction.
     */
    fun flipMessage(isSentFromUser: Boolean) {
        val cardViewParams = view.cardView.layoutParams as RelativeLayout.LayoutParams

        if (isSentFromUser) {
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_END)
            }
        } else {
            cardViewParams.apply {
                removeRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_START)
            }
        }

        view.cardView.layoutParams = cardViewParams
    }
}