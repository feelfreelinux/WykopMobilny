package io.github.feelfreelinux.wykopmobilny.ui.elements.entry_view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.setPhotoViewUrl
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandlerImpl
import kotlinx.android.synthetic.main.feed_layout.view.*

class EntryView : CardView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val callbacks = WykopActionHandlerImpl(context)

    init {
        View.inflate(context, R.layout.feed_layout, this)
        val dpAsPixels = (8 * resources.displayMetrics.density + 0.5f).toInt()
        setContentPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
    }

    fun setEntryData(entry : Entry) {
        setupHeader(entry)
        setupBody(entry)
        setupButtons(entry)
    }

    private fun setupHeader(entry : Entry) {
        authorHeaderView.setAuthorData(entry.author, entry.date)
    }

    private fun setupButtons(entry : Entry) {
        commentsCountTextView.apply {
            text = entry.commentsCount.toString()

            setOnClickListener {
                callbacks.onCommentsClicked(entry.id)
            }
        }

        voteCountTextView.apply {
            setEntryData(entry.id, entry.voteCount)
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
        }

    }

    private fun setupBody(entry : Entry) {
        entryContentTextView.prepareBody(entry.body, callbacks)

        entryImageView.apply {
            isVisible = false
            entry.embed?.apply {
                isVisible = true
                loadImage(preview)
                if (type == "image") setPhotoViewUrl(url)
            }
        }
    }

}