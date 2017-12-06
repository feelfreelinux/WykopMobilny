package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.link_layout.view.*


class LinkViewHolder(val view: View) : RecyclerView.ViewHolder(view), URLClickedListener {
    fun bindView(link : Link) {
        view.apply {
            title.text = link.title
            title.prepareBody(link.title, this@LinkViewHolder)
            image.loadImage(link.preview)
            description.prepareBody(link.description, this@LinkViewHolder)
            description.text = link.description
            diggCountTextView.text = link.voteCount.toString()
            commentsCountTextView.text = link.commentsCount.toString()
            dateTextView.text = link.date.toPrettyDate()
        }
    }

    override fun handleUrl(url: String) {
        // @TODO something here
    }
}