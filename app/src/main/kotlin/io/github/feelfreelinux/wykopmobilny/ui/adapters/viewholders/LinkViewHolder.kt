package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.link_layout.view.*


class LinkViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {
    fun bindView(link : Link) {
        view.apply {
            title.text = link.title.removeHtml()
            image.isVisible = link.preview != null
            link.preview?.let { image.loadImage(link.preview.stripImageCompression()) }
            description.text = link.description.removeHtml()
            diggCountTextView.text = link.voteCount.toString()
            when(link.userVote) {
                "dig" -> showDigged()
                "bury" -> showBurried()
                else -> diggCountTextView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            }
            commentsCountTextView.text = link.commentsCount.toString()
            dateTextView.text = link.date.toPrettyDate()
            hotBadgeStrip.isVisible = link.isHot
            setOnClickListener {
                view.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(view.getActivityContext()!!, link))
            }
        }
    }

    fun showBurried() {
        view.apply {
            diggCountTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.minusPressedColor))
            diggCountTextView.isEnabled = false
            diggCountTextView.isEnabled = true
            setWykopColorDrawable(true)
        }
    }

    fun showDigged() {
        view.apply {
            diggCountTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.plusPressedColor))
            diggCountTextView.isEnabled = false
            setWykopColorDrawable(true)
        }
    }

    fun setWykopColorDrawable(isButtonSelected : Boolean) {
        view.apply {
            if (isButtonSelected) {
                diggCountTextView.setTextColor(Color.WHITE)
                diggCountTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wypok_activ, 0, 0, 0);
            } else {
                diggCountTextView.setTextColor(description.currentTextColor)
                val typedArray = context.obtainStyledAttributes(arrayOf(
                        R.attr.wypokDrawable).toIntArray())
                diggCountTextView.setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(0), null, null, null)
                typedArray.recycle()

            }
        }
    }

}