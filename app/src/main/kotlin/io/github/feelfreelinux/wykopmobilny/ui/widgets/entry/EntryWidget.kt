package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
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

class EntryWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs),  EntryView, URLClickedListener {

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
    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(entry.author, entry.date, entry.app)
    }

    private fun setupButtons() {
        moreOptionsTextView.setOnClickListener { openOptionsMenu() }
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

        if (entry.survey != null) {
            survey.setSurvey(entry.survey!!, entry.id)
        } else survey.isVisible = false
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

    fun removeEntry() {
        presenter.deleteEntry(entry.id)
    }

    fun editEntry() {
        navigator.openEditEntryActivity(getActivityContext()!!, entry.body, entry.id)
    }

    fun copyContent() {
        clipboardHelper.copyTextToClipboard(entry.body.removeHtml())
    }

    fun openOptionsMenu() {
        val popUpMenu = PopupMenu(context, moreOptionsTextView)
        if (userManager.isUserAuthorized()) {
            if (entry.author.nick == userManager.getUserCredentials()!!.login) {
                popUpMenu.menuInflater.inflate(R.menu.entry_menu_authors_loggedin, popUpMenu.menu)
                popUpMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.copy_content -> copyContent()
                        R.id.edit_content -> editEntry()
                        R.id.delete_content -> removeEntry()
                    }
                    true
                }
            } else {
                popUpMenu.menuInflater.inflate(R.menu.entry_menu_loggedin, popUpMenu.menu)
                popUpMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.copy_content -> copyContent()
                    }
                    true
                }
            }
        } else {
            popUpMenu.menuInflater.inflate(R.menu.entry_menu_loggedin, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.copy_content -> copyContent()
                }
                true
            }
        }
        popUpMenu.show()
    }
}