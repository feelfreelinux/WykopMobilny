package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.EntryMenuDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.EntryMenuDialogListener
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.entry_layout.view.*
import javax.inject.Inject

class EntryWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs), EntryMenuDialogListener, EntryView, URLClickedListener {

    @Inject lateinit var linkHandler: WykopLinkHandlerApi
    @Inject lateinit var userManager: UserManagerApi
    @Inject lateinit var clipboardHelper: ClipboardHelperApi
    @Inject lateinit var presenter: EntryPresenter
    @Inject lateinit var navigator: NavigatorApi
    private lateinit var entry: Entry
    var shouldEnableClickListener = true
  
    init {
        WykopApp.uiInjector.inject(this)
        View.inflate(context, R.layout.entry_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    fun setEntryData(entry: Entry) {
        this.entry = entry
        setupHeader()
        setupBody()
        setupButtons()

        setOnLongClickListener {
            EntryMenuDialog(getActivityContext()!!, entry.author, userManager, this).show()
            true
        }
    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(entry.author, entry.date, entry.app)
    }

    private fun setupButtons() {
        commentsCountTextView.apply {
            text = entry.commentsCount.toString()
            if (shouldEnableClickListener) {
                setOnClickListener {
                    navigator.openEntryDetailsActivity(getActivityContext()!!, entry.id)
                }
            }
        }

        if (shouldEnableClickListener) {
            setOnClickListener {
                navigator.openEntryDetailsActivity(getActivityContext()!!, entry.id)
            }
        }

        voteButton.apply {
            setEntryData(entry)
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
        }

        favoriteButton.setEntryData(entry)
    }

    private fun setupBody() {
        entryContentTextView.prepareBody(entry.body, this)
        entryImageView.setEmbed(entry.embed)
    }

    override fun handleUrl(url: String) {
        linkHandler.handleUrl(getActivityContext()!!, url)
    }

    override fun showErrorDialog(e: Throwable) =
            context.showExceptionDialog(e)

    override fun markEntryAsRemoved() {
        entryContentTextView.setText(R.string.entryRemoved)
        entryImageView.isVisible = false
    }

    override fun removeEntry() {
        presenter.deleteEntry(entry.id)
    }

    override fun editEntry() {
        navigator.openEditEntryActivity(getActivityContext()!!, entry.body.removeHtml(), entry.id)
    }

    override fun copyContent() {
        clipboardHelper.copyTextToClipboard(entry.body.removeHtml())
    }
}