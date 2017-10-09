package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.openEntryActivity
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.feed_layout.view.*
import javax.inject.Inject

class EntryView : CardView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var linkHandler : WykopLinkHandlerApi

    init {
        WykopApp.uiInjector.inject(this)
        View.inflate(context, R.layout.feed_layout, this)
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
                context.openEntryActivity(entry.id)
            }
        }

        voteButton.apply {
            setEntryData(entry.id, entry.voteCount)
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
        }

        favoriteButton.setEntryData(entry)
    }

    private fun setupBody(entry : Entry) {
        entryContentTextView.prepareBody(entry.body, linkHandler)
        entryImageView.setEmbed(entry.embed)
    }

}